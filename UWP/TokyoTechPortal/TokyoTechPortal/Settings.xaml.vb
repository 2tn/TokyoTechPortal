' 空白ページの項目テンプレートについては、https://go.microsoft.com/fwlink/?LinkId=234238 を参照してください

Imports Windows.Security.Credentials
''' <summary>
''' それ自体で使用できる空白ページまたはフレーム内に移動できる空白ページ。
''' </summary>
Public NotInheritable Class Settings
    Inherits Page

    Private Const CREDENTIAL_ACCOUNT As String = "TokyoTechPortal"

    Private Sub Page_Loaded(sender As Object, e As RoutedEventArgs)
        Dim vault As New PasswordVault
        Dim credList = vault.RetrieveAll

        For Each c In credList
            c.RetrievePassword()
            Select Case c.UserName
                Case "UserName"
                    username.Text = c.Password
                Case "Password"
                    password.Password = c.Password
                Case "Matrix_1"
                    matrix_1.Password = c.Password
                Case "Matrix_2"
                    matrix_2.Password = c.Password
                Case "Matrix_3"
                    matrix_3.Password = c.Password
                Case "Matrix_4"
                    matrix_4.Password = c.Password
                Case "Matrix_5"
                    matrix_5.Password = c.Password
                Case "Matrix_6"
                    matrix_6.Password = c.Password
                Case "Matrix_7"
                    matrix_7.Password = c.Password
            End Select
            c = Nothing
        Next
        credList = Nothing
        vault = Nothing
    End Sub

    Private Sub saveSettings_Click(sender As Object, e As RoutedEventArgs)
        Dim vault As New PasswordVault
        Dim credList As New List(Of PasswordCredential)

        If username.Text <> String.Empty Then credList.Add(New PasswordCredential(CREDENTIAL_ACCOUNT, "UserName", username.Text))
        If password.Password <> String.Empty Then credList.Add(New PasswordCredential(CREDENTIAL_ACCOUNT, "Password", password.Password))
        If matrix_1.Password <> String.Empty Then credList.Add(New PasswordCredential(CREDENTIAL_ACCOUNT, "Matrix_1", matrix_1.Password))
        If matrix_2.Password <> String.Empty Then credList.Add(New PasswordCredential(CREDENTIAL_ACCOUNT, "Matrix_2", matrix_2.Password))
        If matrix_3.Password <> String.Empty Then credList.Add(New PasswordCredential(CREDENTIAL_ACCOUNT, "Matrix_3", matrix_3.Password))
        If matrix_4.Password <> String.Empty Then credList.Add(New PasswordCredential(CREDENTIAL_ACCOUNT, "Matrix_4", matrix_4.Password))
        If matrix_5.Password <> String.Empty Then credList.Add(New PasswordCredential(CREDENTIAL_ACCOUNT, "Matrix_5", matrix_5.Password))
        If matrix_6.Password <> String.Empty Then credList.Add(New PasswordCredential(CREDENTIAL_ACCOUNT, "Matrix_6", matrix_6.Password))
        If matrix_7.Password <> String.Empty Then credList.Add(New PasswordCredential(CREDENTIAL_ACCOUNT, "Matrix_7", matrix_7.Password))

        For Each c In credList
            vault.Add(c)
            c = Nothing
        Next
        credList = Nothing
        vault = Nothing
    End Sub

End Class
