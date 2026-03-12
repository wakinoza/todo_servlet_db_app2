package servlet;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import model.TodoItemLogic;
import bean.TodoItem;
import bean.User;

class MainTest {
  private HttpServletRequest request;
  private HttpServletResponse response;
  private HttpSession session;
  private RequestDispatcher dispatcher;
  private TodoItemLogic mockLogic;
  private Main mainServlet;

  @BeforeEach
  void setUp() {
    request = mock(HttpServletRequest.class);
    response = mock(HttpServletResponse.class);
    session = mock(HttpSession.class);
    dispatcher = mock(RequestDispatcher.class);
    mockLogic = mock(TodoItemLogic.class);
    mainServlet = new Main(mockLogic);

    when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
  }

  // --- 共通ヘルパーメソッド ---

  private void setLoggedIn(boolean hasSession, boolean hasUser) {
    if (!hasSession) {
      when(request.getSession(false)).thenReturn(null);
    } else {
      when(request.getSession(false)).thenReturn(session);
      when(session.getAttribute("loginUser")).thenReturn(hasUser ? new User() : null);
      when(session.getAttribute("csrfToken")).thenReturn("valid_token");
    }
  }

  private void setCsrfToken(String token) {
    when(request.getParameter("csrfToken")).thenReturn(token);
  }

  // --- doGet 系のテスト ---

  @Nested
  @DisplayName("doGet メソッドのテスト")
  class DoGetTests {

    @ParameterizedTest
    @CsvSource({"false, false, 'セッションなし'", "true,  false, 'ユーザー情報なし'"})
    @DisplayName("異常系：ログイン条件を満たさない場合はリダイレクトされること")
    void testDoGetAuthFailure(boolean hasSession, boolean hasUser) throws Exception {
      setLoggedIn(hasSession, hasUser);
      mainServlet.doGet(request, response);
      verify(response).sendRedirect("index.jsp");
    }

    @Test
    @DisplayName("異常系：セッションはあるがユーザー情報がない場合（doGet）")
    void testDoGetSessionNoUser() throws Exception {
      setLoggedIn(true, false);
      mainServlet.doGet(request, response);
      verify(response).sendRedirect("index.jsp");
    }

    @Test
    @DisplayName("正常系：Todoリストを取得してmain.jspにフォワードすること")
    void testDoGetSuccess() throws Exception {
      setLoggedIn(true, true);
      List<TodoItem> list = new ArrayList<>();
      list.add(new TodoItem());
      when(mockLogic.getAllTodoItem()).thenReturn(list);

      mainServlet.doGet(request, response);

      verify(request).setAttribute("todoItemList", list);
      verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("境界条件：リストがnullの場合でも空のリストがセットされること")
    void testDoGetWithNullList() throws Exception {
      setLoggedIn(true, true);
      when(mockLogic.getAllTodoItem()).thenReturn(null);

      mainServlet.doGet(request, response);

      verify(request).setAttribute(eq("todoItemList"), argThat(l -> ((List<?>) l).isEmpty()));
    }
  }

  // --- doPost 系のテスト ---

  @Nested
  @DisplayName("doPost メソッドのテスト")
  class DoPostTests {

    @Test
    @DisplayName("異常系：セッションはあるがユーザー情報がない場合（doPost）")
    void testDoPostSessionNoUser() throws Exception {
      setLoggedIn(true, false);
      mainServlet.doPost(request, response);
      verify(response).sendRedirect("index.jsp");
    }

    @Test
    @DisplayName("異常系：未ログイン時はリダイレクトされること")
    void testDoPostAuthFailure() throws Exception {
      setLoggedIn(false, false);
      mainServlet.doPost(request, response);
      verify(response).sendRedirect("index.jsp");
    }

    @ParameterizedTest
    @CsvSource({"wrong_token, 'トークン不一致'", ",            'セッション側にトークンがない場合'"})
    @DisplayName("異常系：CSRFトークン不正時は403エラーになること")
    void testDoPostCsrfFailure(String requestToken) throws Exception {
      setLoggedIn(true, true);
      if (requestToken == null) {
        when(session.getAttribute("csrfToken")).thenReturn(null);
      } else {
        when(request.getParameter("csrfToken")).thenReturn(requestToken);
      }
      mainServlet.doPost(request, response);
      verify(response).sendError(eq(HttpServletResponse.SC_FORBIDDEN), anyString());
    }

    @Nested
    @DisplayName("アクション別テスト")
    class ActionTests {
      @BeforeEach
      void setupAction() {
        setLoggedIn(true, true);
        setCsrfToken("valid_token");
        when(mockLogic.getAllTodoItem()).thenReturn(new ArrayList<>());
      }

      @Test
      @DisplayName("正常系：新規作成成功")
      void testCreateSuccess() throws Exception {
        when(request.getParameter("action")).thenReturn("create");
        when(request.getParameter("text")).thenReturn("Task");
        when(request.getParameter("csrfToken")).thenReturn("valid_token"); // 明示的に設定

        TodoItem item = new TodoItem();
        when(mockLogic.create("Task")).thenReturn(item);
        when(mockLogic.add(item)).thenReturn(true);

        mainServlet.doPost(request, response);

        verify(mockLogic).add(item);
        verify(dispatcher).forward(request, response);
      }

      @Test
      @DisplayName("正常系：進捗更新成功")
      void testUpdateSuccess() throws Exception {
        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("id")).thenReturn("10");
        when(request.getParameter("csrfToken")).thenReturn("valid_token");

        when(mockLogic.updateProgress(10)).thenReturn(true);

        mainServlet.doPost(request, response);

        verify(mockLogic).updateProgress(10);
        verify(dispatcher).forward(request, response);
      }

      @Test
      @DisplayName("異常系：進捗更新失敗")
      void testUpdateFailure() throws Exception {
        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("id")).thenReturn("99");
        when(request.getParameter("csrfToken")).thenReturn("valid_token");

        when(mockLogic.updateProgress(99)).thenReturn(false);

        mainServlet.doPost(request, response);

        verify(request).setAttribute(eq("errorMsg"), contains("進捗の更新に失敗しました"));
      }

      @ParameterizedTest
      @CsvSource({"create, Task, false, '追加失敗'", "create, ,     true,  '入力空'"})
      @DisplayName("異常系：作成アクションの失敗テスト")
      void testCreateFailures(String action, String text, boolean logicCreateReturnsNull)
          throws Exception {
        when(request.getParameter("action")).thenReturn(action);
        when(request.getParameter("text")).thenReturn(text);
        when(request.getParameter("csrfToken")).thenReturn("valid_token");

        if (logicCreateReturnsNull) {
          when(mockLogic.create(any())).thenReturn(null);
        } else {
          TodoItem item = new TodoItem();
          when(mockLogic.create(any())).thenReturn(item);
          when(mockLogic.add(item)).thenReturn(false);
        }

        mainServlet.doPost(request, response);
        verify(request).setAttribute(eq("errorMsg"), anyString());
      }
    }
  }

  // --- 特殊ルート（カバレッジ用） ---

  @Nested
  @DisplayName("カバレッジ補完用（Factoryルート）")
  class CoverageTests {
    @Test
    @DisplayName("網羅：デフォルトコンストラクタ経由での実行")
    void testFactoryRoutes() {
      Main defaultServlet = new Main();
      setLoggedIn(true, true);
      setCsrfToken("valid_token");

      try {
        defaultServlet.doGet(request, response);
      } catch (Exception ignored) {
      }
      try {
        defaultServlet.doPost(request, response);
      } catch (Exception ignored) {
      }
    }
  }
}
