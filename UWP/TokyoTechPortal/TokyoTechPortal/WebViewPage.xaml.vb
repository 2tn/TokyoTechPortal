' 空白ページの項目テンプレートについては、https://go.microsoft.com/fwlink/?LinkId=234238 を参照してください

Imports System.Text.RegularExpressions
Imports Windows.Foundation
Imports Windows.Security.Credentials
Imports Windows.Web.Http
Imports Windows.Web.Http.Headers
''' <summary>
''' それ自体で使用できる空白ページまたはフレーム内に移動できる空白ページ。
''' </summary>
Public NotInheritable Class WebViewPage
    Inherits Page

    Private Const CREDENTIAL_ACCOUNT As String = "TokyoTechPortal"
    Private Async Sub Password_SubmitAsync()

        Dim vault As New PasswordVault
        Dim cred As PasswordCredential
        cred = vault.Retrieve(CREDENTIAL_ACCOUNT, "UserName")
        cred.RetrievePassword()
        Dim UserName As String = cred.Password
        cred = vault.Retrieve(CREDENTIAL_ACCOUNT, "Password")
        cred.RetrievePassword()
        Dim Password As String = cred.Password
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
        UserName = "123456789"
        Password = "123456789012345678901234567890"
    End Sub

    Private Async Sub Matix_SubmitAsync()
        Dim matrixChars(2) As Char
        Dim matrix As String = ""
        For i = 1 To 7
            Dim vault As New PasswordVault
            Dim cred As PasswordCredential
            cred = vault.Retrieve(CREDENTIAL_ACCOUNT, "Matrix_" & i)
            cred.RetrievePassword()
            matrix &= cred.Password
        Next
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
                matrixChars(i) = Matrix(value * 10 + name)
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

        matrix = "0123456789012345678901234567890123456789012345678901234567890123456789"
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

    Public Sub WebView_GoBack()
        mainWebView.GoBack()
    End Sub

End Class
