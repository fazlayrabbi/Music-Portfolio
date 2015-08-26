<%@ Page Title="" Language="C#" MasterPageFile="~/Site.Master" AutoEventWireup="true" CodeBehind="Privacy.aspx.cs" Inherits="mp.Privacy" %>
<asp:Content ID="Content1" ContentPlaceHolderID="MainContent" runat="server">
    <table style="width: 100%">
        <tr>
            <td>
                <asp:Label ID="Label1" runat="server" Text="Privacy Policy" Font-Bold="True" Font-Size="X-Large"></asp:Label>
            </td>
        </tr>
        <tr>
            <td>
                <asp:TextBox ID="TextBox1" runat="server" TextMode="MultiLine" Rows="10" Text="Our privacy policy is simple.
We do not save any posts from your facebook profile locally in our database.
Your posts will be displayed on our site for visitors as part of your portfolio.
No information is sold to 3rd parties.
You can revoke this app's access at any time.

This is a non commercial product, being created as a college project." Width="100%" BorderStyle="None" ReadOnly="True"></asp:TextBox>
            </td>
        </tr>
    </table>
</asp:Content>
