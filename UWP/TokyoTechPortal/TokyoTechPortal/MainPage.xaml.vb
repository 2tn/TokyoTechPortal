Imports System.Text.RegularExpressions
Imports Windows.Web.Http
Imports Windows.Web.Http.Headers

Public NotInheritable Class MainPage
    Inherits Page
    Private Sub Page_Loaded(sender As Object, e As RoutedEventArgs)
        Core.CoreApplication.GetCurrentView().TitleBar.ExtendViewIntoTitleBar = True
        ApplicationView.GetForCurrentView().TitleBar.ButtonBackgroundColor = Windows.UI.Colors.Transparent
        Window.Current.SetTitleBar(titleBar)
        mainFrame.Navigate(GetType(WebViewPage))
    End Sub

    Private Sub ListViewItem_Tapped(sender As ListViewItem, e As TappedRoutedEventArgs)
        If mainFrame.Content.GetType IsNot GetType(WebViewPage) Then
            mainFrame.Navigate(GetType(WebViewPage))
        End If
        Select Case sender.Content
            Case "東工大ポータルメニュー"
                CType(mainFrame.Content, WebViewPage).WebView_Navigate(New Uri("https://portal.nap.gsic.titech.ac.jp/GetAccess/ResourceList"))
            Case "Tokyo Tech Mail"
                CType(mainFrame.Content, WebViewPage).WebView_Navigate(New Uri("https://mail.m.titech.ac.jp:443/cgi-bin/loginweb"))
            Case "教務Webシステム"
                CType(mainFrame.Content, WebViewPage).WebView_Navigate(New Uri("https://kyomu2.gakumu.titech.ac.jp:443/Titech/Default.aspx"))
            Case "TOKYO TECH OCW-i"
                CType(mainFrame.Content, WebViewPage).WebView_Navigate(New Uri("https://secure.ocw.titech.ac.jp/ocwi/index.php"))
            Case "図書館サービス:TDL Online Request"
                CType(mainFrame.Content, WebViewPage).WebView_Navigate(New Uri("https://request.libra.titech.ac.jp:443/portal/user.php?plang=jpn "))
            Case "TSUBAME2.5利用ポータル"
                CType(mainFrame.Content, WebViewPage).WebView_Navigate(New Uri("https://portal.g.gsic.titech.ac.jp:443/portal/sso/"))
            Case "東工大学修ポートフォリオ"
                CType(mainFrame.Content, WebViewPage).WebView_Navigate(New Uri("https://portfolio.gakumu.titech.ac.jp:443/"))
            Case "学認"
                CType(mainFrame.Content, WebViewPage).WebView_Navigate(New Uri("https://idp-gakunin.nap.gsic.titech.ac.jp:443/gakuninlink/service/"))
            Case "パスワード変更"
                CType(mainFrame.Content, WebViewPage).WebView_Navigate(New Uri("https://rp.nap.gsic.titech.ac.jp:443/changepwd/personal/login"))
            Case "タイムアウト設定"
                CType(mainFrame.Content, WebViewPage).WebView_Navigate(New Uri("https://rp.nap.gsic.titech.ac.jp:443/CGI/User_Timeout/User_Timeout.php"))
            Case "Mailing List"
                CType(mainFrame.Content, WebViewPage).WebView_Navigate(New Uri("https://rp.nap.gsic.titech.ac.jp:443/DeepMail/cgi-bin/sso.cgi"))
        End Select
        splitView.IsPaneOpen = False
        title.Text = sender.Content
    End Sub

    Private Sub AppBarButton_Tapped(sender As Object, e As TappedRoutedEventArgs)
        mainFrame.Navigate(GetType(Settings))
        splitView.IsPaneOpen = False
        title.Text = "設定"
    End Sub

    Private Sub backButton_Click(sender As Object, e As RoutedEventArgs)
        If TypeOf mainFrame.Content Is WebViewPage Then
            CType(mainFrame.Content, WebViewPage).WebView_GoBack()
        Else
            mainFrame.GoBack()
        End If
    End Sub
End Class
