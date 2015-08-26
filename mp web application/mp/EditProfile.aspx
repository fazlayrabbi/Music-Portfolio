<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="EditProfile.aspx.cs" Inherits="mp.EditProfile" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title></title>
    <link href="Content/MPProfilePage.css" rel="stylesheet" />

  <%--<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css"/>--%>
    <link href="Content/bootstrap.min.css" rel="stylesheet" />
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
    <script src="Scripts/jquery-1.10.2.min.js"></script>
    <script src="http://connect.soundcloud.com/sdk.js"></script>
    <style type="text/css">
        .auto-style1 {
            width: 85px;
        }
        .auto-style2 {
            width: 332px;
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
    <form id="form1" runat="server" CssClass="form-control" role="form">
        <asp:ScriptManager ID='ScriptManager1' runat='server' EnablePageMethods='true' />
        
                        <div class="formLabel">
                        <asp:Image ID="img_profilepic" runat="server" ImageUrl="" Height="150px" Weidth="150px"/>
                        </div>
                        <div class="formLabel">
                        <asp:Label ID="Label6" runat="server" CssClass="control-label col-sm-2" Text="Profile Picture"></asp:Label>
                        </div>
                        <div class="col-sm-4">
                        <asp:FileUpload ID="upload_profilepic" runat="server" />
                        </div>
    
                        <div class="formLabel">
                        <asp:Label ID="Label1" CssClass="control-label col-sm-2" runat="server" Text="Name"></asp:Label>
                        </div>
                        <div class="col-sm-4">
                        <asp:TextBox ID="txt_name" CssClass="form-control" runat="server"></asp:TextBox>
                        </div>
                        <div class="formLabel">
                        <asp:Label ID="Label2" CssClass="control-label col-sm-2" runat="server" Text="Genre"></asp:Label>
                        </div>
                        <div class="col-sm-4">
                        <asp:TextBox ID="txt_genre" CssClass="form-control" runat="server"></asp:TextBox>
                        </div>
                        <div class="formLabel">
                        <asp:Label ID="Label3" CssClass="control-label col-sm-2" runat="server" Text="Agent"></asp:Label>
                        </div>
                        <div class="col-sm-4">
                        <asp:TextBox ID="txt_agent" CssClass="form-control" runat="server"></asp:TextBox>
                        </div>
                        <div class="formLabel">
                        <asp:Label ID="Label4" CssClass="control-label col-sm-2" runat="server" Text="Phone"></asp:Label>
                        </div>
                        <div class="col-sm-4">
                        <asp:TextBox ID="txt_phone" CssClass="form-control" runat="server"></asp:TextBox>
                        </div>
                        <div class="formLabel">
                        <asp:Label ID="Label5" CssClass="control-label col-sm-2" runat="server" Text="Biography"></asp:Label>
                        </div>
                        <div class="col-sm-6">
                        <asp:TextBox ID="txt_bio" CssClass="form-control" runat="server" TextMode="MultiLine" Rows="10"></asp:TextBox>
                        </div>
                        <div class="formLabel">
                        <div class="col-sm-8">
                        <asp:Button ID="cmd_save_profile" runat="server" CssClass="btn btn-sm" Text="Save" width="100%" OnClick="cmd_save_profile_Click" />
                        </div></div>
                

                        <div class="formLabel">
                        <asp:Button ID="cmd_twitter_login" runat="server" CssClass="btn btn-sm" Text="Login to Twitter" OnClick="cmd_twitter_login_Click" />
                        <button id="connect" class="btn btn-sm" type="button"  >Login to SounndCloud</button>
                        <asp:Button ID="cmd_insta_login" runat="server" CssClass="btn btn-sm" Text="Login to Instagram" OnClick="cmd_insta_login_Click" />
                    
                        <asp:Button ID="cmd_fb_login" runat="server" CssClass="btn btn-sm" Text="Login to Facebook" OnClick="cmd_fb_login_Click" />
                    
                        
                        <%--<asp:Button ID="cmd_sc_login" runat="server" Text="Login to SoundCloud" OnClientClick="SC.connect()"  />--%>
                        <%--<a href="#" class="big button" id="connect">Login to SoundCloud</a>--%>
                        
                        </div>
                        <%--                    <div class="logged-in" style="display: one;">
                      <p>
                      Logged in as: <span id="username"></span>
                      </p>
                      Your profile description:
                      <input type="text" id="description" class="fullWidth" />
                      <button id="update" class="big button">Update your profile description</button>
                    </div>--%>
                        <div class="formLabel">
                        <asp:Label ID="Label7" CssClass="control-label col-sm-6" runat="server" Text="Upload local media" Font-Names="Verdana"></asp:Label>
                   
                        
                       
                        <asp:FileUpload ID="up_local_media" runat="server" Width="100%" />
                       
                    
                        <asp:Button ID="cmd_upload" runat="server" CssClass="btn btn-sm" Text="Upload" OnClick="cmd_upload_Click" />
                        <div class="col-sm-4">
                        <asp:TextBox ID="txt_up_status" ReadOnly="true"  runat="server" Width="100%" BorderStyle="None"></asp:TextBox>
                        </div>
                        </div>

        
    </form>
         </div>
        <div class="footer">
        <span><strong>© Music Portfolio 2015</strong></span>
    </div>
    </div>
    
    
    

    <script>
        SC.initialize({
            client_id: "238aaf891d058f2f387fd6883dd4301c",
            redirect_uri: "http://mp.makhdumi.net/callback.html"
        });

        $("#connect").click(function () {
            SC.connect(function () {
                SC.get("/me", function (me) {
                    //SC.oEmbed("http://soundcloud.com/" + me.username ,{color:"ff0066"},document.getElementById("soundcloud"));
                    window.location = "http://mp.makhdumi.net/musician.aspx?sc_id=" + me.id;
                    //var user_url = 'https://soundcloud.com/'+me.username;
                    //SC.oEmbed(user_url, { auto_play: false }, function (oEmbed) {
                    //    console.log('oEmbed response: ' + oEmbed);
                    //console.log(me);
                });

                //$("#username").text(me.username);
                //$("#description").val(me.description);
            });
        });
        //});

        $("#update").click(function () {
            SC.put("/me", { user: { description: $("#description").val() } }, function (response, error) {
                if (error) {
                    alert("Some error occured: " + error.message);
                } else {
                    alert("Profile description updated!");
                }
            });
        });
    </script>

</body>
</html>
