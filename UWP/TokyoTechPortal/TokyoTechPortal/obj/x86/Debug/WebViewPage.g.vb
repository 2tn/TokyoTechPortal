﻿#ExternalChecksum("C:\Users\Yusuke\Documents\Visual Studio 2017\Repos\TokyoTechPortal\UWP\TokyoTechPortal\TokyoTechPortal\WebViewPage.xaml", "{406ea660-64cf-4c82-b6f0-42d48172a799}", "C23055C68544E6F376075714E1CBC8E1")
'------------------------------------------------------------------------------
' <auto-generated>
'     This code was generated by a tool.
'
'     Changes to this file may cause incorrect behavior and will be lost if
'     the code is regenerated.
' </auto-generated>
'------------------------------------------------------------------------------

Option Strict Off
Option Explicit On

Namespace Global.TokyoTechPortal

    Partial Class WebViewPage
        Implements Global.Windows.UI.Xaml.Markup.IComponentConnector
        Implements Global.Windows.UI.Xaml.Markup.IComponentConnector2


        <Global.System.CodeDom.Compiler.GeneratedCodeAttribute("Microsoft.Windows.UI.Xaml.Build.Tasks", "14.0.0.0")>  _
        <Global.System.Diagnostics.DebuggerNonUserCodeAttribute()>  _
        Public Sub Connect(ByVal connectionId As Integer, ByVal target As Global.System.Object) Implements Global.Windows.UI.Xaml.Markup.IComponentConnector.Connect
            Select Case connectionid
            Case 1
                    Dim element1 As Global.Windows.UI.Xaml.Controls.Page = CType(target, Global.Windows.UI.Xaml.Controls.Page)
#ExternalSource("..\..\..\WebViewPage.xaml",9)
                AddHandler DirectCast(element1, Global.Windows.UI.Xaml.Controls.Page).Loaded, AddressOf Me.Page_LoadedAsync
#End ExternalSource
                    Exit Select
            Case 2
                    Me.mainWebView = CType(target, Global.Windows.UI.Xaml.Controls.WebView)
                    Exit Select
                    Case Else
                        Exit Select
            End Select
                Me._contentLoaded = true
        End Sub
        <Global.System.CodeDom.Compiler.GeneratedCodeAttribute("Microsoft.Windows.UI.Xaml.Build.Tasks", "14.0.0.0")>  _
        <Global.System.Diagnostics.DebuggerNonUserCodeAttribute()>  _
        Public Function GetBindingConnector(connectionId As Integer, target As Object) As Global.Windows.UI.Xaml.Markup.IComponentConnector Implements Global.Windows.UI.Xaml.Markup.IComponentConnector2.GetBindingConnector
            Dim returnValue As Global.Windows.UI.Xaml.Markup.IComponentConnector = Nothing
            Return returnValue
        End Function
    End Class

End Namespace

