<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Default.aspx.cs" Inherits="mp.Default1" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title></title>
  <meta charset="utf-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <%--<link rel="stylesheet" href="/Content/ProfilePage.css"/>--%>
    <link href="Content/MPProfilePage.css" rel="stylesheet" />

  <%--<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css"/>--%>
    <link href="Content/bootstrap.min.css" rel="stylesheet" />
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
    <script src="http://connect.soundcloud.com/sdk.js"></script>
    <%--<script src="https://w.soundcloud.com/player/api.js" type="text/javascript"></script>--%>
    <%--<script src="/Scripts/youtube.js"></script>--%>
    
    <%--<script src="/Scripts/ProfilePage.js" type="text/javascript"></script>--%>
    <script src="Scripts/MPProfilePage.js"></script>

         <script type="text/javascript">
             function imgclick(userid) { window.location = "/loadmusician.aspx?userid=" + userid; }
        </script>


    <style>
        td
        {
            padding:0 15px 0 15px;
        }

        button 
        {
            border:none;
            outline:0;
            color:#FFFFFF;
            background-color:#FFFFFF;
        }
        .artistInfo{
                margin-top: 15px;
               
            }
        @media (max-width: 564px){
            .artistInfo{
                clear: both;
            }
        }
    </style>
</head>
<body>
    

        <div id="search" class="container">
            <nav class="navbar navbar-inverse">
      <div class="container-fluid">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>                        
          </button>
          <a class="navbar-brand" href="#">Music Portfolio</a>
        </div>
        <div class="collapse navbar-collapse" id="myNavbar">
          <ul class="nav navbar-nav">
            <li class="active"><a href="/Default.aspx">Home</a></li>
        
            <li><a href="#">About</a></li>
            <li><a href="#">Contact</a></li>
          </ul>
          <ul class="nav navbar-nav navbar-right">
<%--            <li class="dropdown">
              <a class="dropdown-toggle" data-toggle="dropdown" href="#">Musicians <span class="caret"></span></a>
              <ul class="dropdown-menu">
                <li><a href="#">Page 1-1</a></li>
                <li><a href="#">Page 1-2</a></li>
                <li><a href="#">Page 1-3</a></li>
              </ul>
            </li>--%>
            <li><a href="/Account/Signup.aspx"><span class="glyphicon glyphicon-user"></span> Sign Up</a></li>
            <li><a href="/Account/Login.aspx"><span class="glyphicon glyphicon-log-in"></span> Login</a></li>
            <li><a href="/Account/Logout.aspx"><span class="glyphicon glyphicon-log-out"></span> Logout</a></li>
          </ul>
        </div>
      </div>
    </nav>
    
        <div class="jumbotron">
            <form  id="form1" runat="server">
            <table class="table-responsive">
                <tr>
                    <td colspan="2">
                        <p>Show results for</p>
                    </td>
                </tr>
                <tr>
                    <td>
                        <p>Artist</p>
                    </td>
                    <td>
                        <asp:TextBox ID="txt_artist" runat="server" CssClass="form-control"></asp:TextBox>
                    </td>
                </tr>
                <tr>
                    <td>
                        <p>Genre</p>
                    </td>
                    <td>
                        <asp:DropDownList ID="ddl_Genre" runat="server" CssClass="form-control">
                            <asp:ListItem></asp:ListItem>
                            <asp:ListItem>Alternative</asp:ListItem>
                            <asp:ListItem>Blues</asp:ListItem>
                            <asp:ListItem>Classical</asp:ListItem>
                            <asp:ListItem>Country</asp:ListItem>
                            <asp:ListItem>Dance</asp:ListItem>
                            <asp:ListItem>Inspirational</asp:ListItem>
                            <asp:ListItem>Jazz</asp:ListItem>
                            <asp:ListItem>New Age</asp:ListItem>
                            <asp:ListItem>Opera</asp:ListItem>
                            <asp:ListItem>Pop</asp:ListItem>
                            <asp:ListItem>R&amp;B Soul</asp:ListItem>
                            <asp:ListItem>Rock</asp:ListItem>
                        </asp:DropDownList>
                    </td>
                </tr>
                <tr>
                    <td >
                        <asp:Button  ID="cmd_search" runat="server" Text="Filter" CssClass="btn btn-sm" OnClick="cmd_search_Click" />
                    </td>
                    <td>
                        <asp:Label ID="lbl_result" ForeColor="Red" runat="server" BorderStyle="None" Text=""></asp:Label>
                    </td>
                </tr>
            </table>
                </form>
        </div>
            <div class="row">
                <div class="col-lg-12" id="music" runat="server">
                
                </div>

            </div>
            <div class="footer">
        <span><strong>© Music Portfolio 2015</strong></span>
    </div>
        </div>

         


</body>

</html>
