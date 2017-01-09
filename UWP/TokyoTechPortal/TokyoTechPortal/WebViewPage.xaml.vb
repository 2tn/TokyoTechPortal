' 空白ページの項目テンプレートについては、https://go.microsoft.com/fwlink/?LinkId=234238 を参照してください

Imports System.Text.RegularExpressions
Imports Windows.Foundation
Imports Windows.Web.Http
Imports Windows.Web.Http.Headers
''' <summary>
''' それ自体で使用できる空白ページまたはフレーム内に移動できる空白ページ。
''' </summary>
Public NotInheritable Class WebViewPage
    Inherits Page
    Private Async Sub Password_SubmitAsync()
        Try
            Dim keyString As String = Await mainWebView.InvokeScriptAsync("eval", New String() {
                                                                          "(function(){
                                                                            var len = document.login.elements.length;
                                                                            var str = '';
                                                                            for (var i = 0; i < len; i++){
                                                                                str += document.login.elements[i].name + '=';
                                                                                switch (document.login.elements[i].name){
                                                                                    case ""usr_name"":
                                                                                        str += """ & UserName & """ + '&';
                                                                                        break;
                                                                                    case ""usr_password"":
                                                                                        str += """ & Password & """ + '&';
                                                                                        break;
                                                                                    default:
                                                                                        str += document.login.elements[i].value + '&';
                                                                                        break;
                                                                                }
                                                                            }
                                                                            return str;
                                                                          }())"})
            Dim cookieString As String = Await mainWebView.InvokeScriptAsync("eval", New String() {
                                                                             "(function(){
                                                                                var str = document.cookie
                                                                                return str;
                                                                             }())"})
            Dim post As New HttpRequestMessage(HttpMethod.Post, New Uri("https://portal.nap.gsic.titech.ac.jp/GetAccess/Login"))
            post.Headers.Referer = New Uri("https://portal.nap.gsic.titech.ac.jp/GetAccess/Login?Template=userpass_key&AUTHMETHOD=UserPassword")
            post.Headers.Cookie.Add(New HttpCookiePairHeaderValue(cookieString.Split("=")(0), cookieString.Split("=")(1)))
            post.Content = New HttpStringContent(keyString)
            post.Content.Headers.ContentType = New HttpMediaTypeHeaderValue("application/x-www-form-urlencoded")
            mainWebView.NavigateWithHttpRequestMessage(post)
        Catch ex As Exception
            Debug.WriteLine(ex.Message)
        End Try
    End Sub

    Private Async Sub Matix_SubmitAsync()
        Dim matrixChars(2) As Char
        Try
            Dim htmlString As String = Await mainWebView.InvokeScriptAsync("eval", New String() {
                                                                           "(function(){
                                                                                var str = document.login.innerHTML; 
                                                                                return str;
                                                                            }())"})
            Dim r As Regex = New Regex("\[(?<row>.*?),(?<column>.*?)\]</th>")
            Dim m As Match = r.Match(htmlString)
            For i As Integer = 0 To 2
                Dim name As Integer = AscW(m.Groups(1).Value(0)) - 65
                Dim value As Integer = m.Groups(2).Value - 1
                m = m.NextMatch()
                matrixChars(i) = Matrix(name * 7 + value)
            Next
            Dim keyString As String = Await mainWebView.InvokeScriptAsync("eval", New String() {
                                                                          "(function(){
                                                                                var len = document.login.elements.length;
                                                                                var str = '';
                                                                                for (var i = 0; i < len; i++){
                                                                                    str += document.login.elements[i].name + '=';
                                                                                    switch (document.login.elements[i].name){
                                                                                        case ""message3"":
                                                                                            str += """ & matrixChars(0) & """ + '&';
                                                                                            break;
                                                                                        case ""message4"":
                                                                                            str += """ & matrixChars(1) & """ + '&';
                                                                                            break;
                                                                                        case ""message5"":
                                                                                            str += """ & matrixChars(2) & """ + '&';
                                                                                            break;
                                                                                        default:
                                                                                            str += document.login.elements[i].value + '&';
                                                                                            break;
                                                                                    }
                                                                                }
                                                                                return str;
                                                                          }())"})
            Dim cookieString As String = Await mainWebView.InvokeScriptAsync("eval", New String() {
                                                                             "(function(){
                                                                                var str = document.cookie
                                                                                return str;
                                                                             }())"})
            Dim post As New HttpRequestMessage(HttpMethod.Post, New Uri("https://portal.nap.gsic.titech.ac.jp/GetAccess/Login"))
            post.Headers.Referer = New Uri("https://portal.nap.gsic.titech.ac.jp/GetAccess/Login?Template=idg_key&AUTHMETHOD=IG&GASF=CERTIFICATE,IG.GRID&LOCALE=ja_JP&GAREASONCODE=13&GAIDENTIFICATIONID=UserPassword&GARESOURCEID=resourcelistID2&GAURI=https://portal.nap.gsic.titech.ac.jp/GetAccess/ResourceList&Reason=13&APPID=resourcelistID2&URI=https://portal.nap.gsic.titech.ac.jp/GetAccess/ResourceList")
            For Each c As String In cookieString.Split(New String() {"; "}, StringSplitOptions.None)
                post.Headers.Cookie.Add(New HttpCookiePairHeaderValue(c.Split("=")(0), c.Split("=")(1)))
            Next
            post.Content = New HttpStringContent(keyString)
            post.Content.Headers.ContentType = New HttpMediaTypeHeaderValue("application/x-www-form-urlencoded")
            mainWebView.NavigateWithHttpRequestMessage(post)
        Catch ex As Exception
            Debug.WriteLine(ex.Message)

        End Try
    End Sub

    Private Async Sub Page_LoadedAsync(sender As Object, e As RoutedEventArgs)
        mainWebView.Visibility = Visibility.Collapsed
        Dim IsNavigationCompleted As Boolean = False
        Dim handler As TypedEventHandler(Of WebView, WebViewNavigationCompletedEventArgs) = Sub()
                                                                                                IsNavigationCompleted = True
                                                                                            End Sub
        AddHandler mainWebView.NavigationCompleted, handler
        mainWebView.Navigate(New Uri("https://portal.nap.gsic.titech.ac.jp/GetAccess/Login?Template=userpass_key&AUTHMETHOD=UserPassword"))
        Await Task.Run(Sub()
                           While Not IsNavigationCompleted
                           End While
                           IsNavigationCompleted = False
                       End Sub)
        Password_SubmitAsync()
        Await Task.Run(Sub()
                           While Not IsNavigationCompleted
                           End While
                           IsNavigationCompleted = False
                       End Sub)
        Matix_SubmitAsync()
        Await Task.Run(Sub()
                           While Not IsNavigationCompleted
                           End While
                           IsNavigationCompleted = False
                       End Sub)
        mainWebView.Visibility = Visibility.Visible
        RemoveHandler mainWebView.NavigationCompleted, handler
    End Sub

    Public Sub WebView_Navigate(uri As Uri)
        mainWebView.Navigate(uri)
    End Sub

    Private _username As String
    Public Property UserName() As String
        Get
            Return _username
        End Get
        Set(ByVal value As String)
            _username = value
        End Set
    End Property

    Private _password As String
    Public Property Password() As String
        Get
            Return _password
        End Get
        Set(ByVal value As String)
            _password = value
        End Set
    End Property

    Private _matrix As String
    Public Property Matrix() As String
        Get
            Return _matrix
        End Get
        Set(ByVal value As String)
            _matrix = value
        End Set
    End Property
End Class
