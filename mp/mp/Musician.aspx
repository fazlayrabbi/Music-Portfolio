<%@ Page Title="Musician's Portfolio" Language="C#" AutoEventWireup="true" CodeBehind="Musician.aspx.cs" Inherits="mp.Default" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title></title>
  <meta charset="utf-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <%--<link rel="stylesheet" href="/Content/ProfilePage.css"/>--%>
    <link href="Content/MPProfilePage.css" rel="stylesheet" />
    <link href="Content/bootstrap.css" rel="stylesheet" />
    <link href="Content/bootstrap.min.css" rel="stylesheet" />
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
    <script src="http://connect.soundcloud.com/sdk.js"></script>
    <%--<script src="https://w.soundcloud.com/player/api.js" type="text/javascript"></script>--%>
    <%--<script src="/Scripts/youtube.js"></script>--%>
    
    <%--<script src="/Scripts/ProfilePage.js" type="text/javascript"></script>--%>
    <script src="Scripts/MPProfilePage.js"></script>



    <script type="text/javascript">
        function myFunction() { alert("sadasd"); }
    </script>

</head>
<body runat="server">


<div class="container">
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
<%--        <li class="dropdown">
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
    <div class="left_float"> 
    <span><br /></span>
    <div class="buttons" align="center">
        <%--
    <button type="button" id="Localbutton" class="btn btn-primary disabled">Local</button>
    <button type="button" id="FBbutton" class="btn btn-primary active">Facebook</button>
        
    <button type="button" id="Twitterbutton" class="btn btn-primary active">Twitter</button>
    <button type="button" id="Instabutton" class="btn btn-primary active">Instagram</button>
    <button type="button" id="Soundcloudbutton" class="btn btn-primary active">Soundcloud</button>
    <button type="button" id="Youtubebutton" class="btn btn-primary active">Youtube</button>--%>

    <%--Don't remove this div --%>
    <ul class="nav nav-pills">
  <li class="active"><a data-toggle="tab" href="#localList">Local</a></li>
  <li><a data-toggle="tab" href="#facebook">Facebook</a></li>
  <li><a data-toggle="tab" href="#insta">Instagram</a></li>
  <li><a data-toggle="tab" href="#tweetList"">Twitter</a></li>
  <li><a data-toggle="tab" href="#soundcloud">SoundCloud</a></li>
</ul>
    <div id="neverdisplay">
    </div>


    </div>
        <br />
        <div class="tab-content">
        <div id="facebook" class="tab-pane fade">
            <div class="ScrollStyle" >
                    <div class="fbstatus" id="fbstatus" runat="server"></div>
                    
                </div>
        </div>
        <div id="insta" align="center" class="tab-pane fade">
      
            <div class="ScrollStyle">
                 <div class="instapics" id="instapics" runat="server"></div>
                  <%--<button type="button" id="instaLoad" class="btn btn-primary active">Load more</button>--%>  
             </div>
        </div>
        <div id="tweetList" class="tab-pane fade" >
        <div id="twitter" class="ScrollStyle" runat="server">
         </div>
            

        </div>
                <div id="soundcloud" runat="server" class="tab-pane fade">
                </div>
        <div id="localList" class="tab-pane fade in active" >
        <div id="local" runat="server" class="ScrollStyle">
            <p></p>
        </div>
        </div>
<%--        <div id="youtube">
            <iframe id="youtubeframe"></iframe>
            <div class="ScrollStyleyt" >
            <a href="#" onclick="changeframe(0)"  id="popoverData0" rel="popover" data-placement="bottom" data-original-title="" data-trigger="hover"><img id="thumbnail0" src="" class="ytthumbnail"/></a>
            <a href="#" onclick="changeframe(1)" id="popoverData1" rel="popover" data-placement="bottom" data-original-title="" data-trigger="hover"><img id="thumbnail1" src="" class="ytthumbnail"/></a>
            <a href="#" onclick="changeframe(2)" id="popoverData2" rel="popover" data-placement="bottom" data-original-title="" data-trigger="hover"><img id="thumbnail2" src="" class="ytthumbnail"/></a>
            <a href="#" onclick="changeframe(3)" id="popoverData3" rel="popover" data-placement="bottom" data-original-title="" data-trigger="hover"><img id="thumbnail3" src="" class="ytthumbnail"/></a>
            <a href="#" onclick="changeframe(4)" id="popoverData4" rel="popover" data-placement="bottom" data-original-title="" data-trigger="hover"><img id="thumbnail4" src="" class="ytthumbnail"/></a>
            <a href="#" onclick="changeframe(5)" id="popoverData5" rel="popover" data-placement="bottom" data-original-title="" data-trigger="hover"><img id="thumbnail5" src="" class="ytthumbnail"/></a>
            <a href="#" onclick="changeframe(6)" id="popoverData6" rel="popover" data-placement="bottom" data-original-title="" data-trigger="hover"><img id="thumbnail6" src="" class="ytthumbnail"/></a>
            <a href="#" onclick="changeframe(7)" id="popoverData7" rel="popover" data-placement="bottom" data-original-title="" data-trigger="hover"><img id="thumbnail7" src="" class="ytthumbnail"/></a>
            <a href="#" onclick="changeframe(8)" id="popoverData8" rel="popover" data-placement="bottom" data-original-title="" data-trigger="hover"><img id="thumbnail8" src="" class="ytthumbnail"/></a>
            <a href="#" onclick="changeframe(9)" id="popoverData9" rel="popover" data-placement="bottom" data-original-title="" data-trigger="hover"><img id="thumbnail9" src="" class="ytthumbnail"/></a>
            <a href="#" onclick="changeframe(10)" id="popoverData10" rel="popover" data-placement="bottom" data-original-title="" data-trigger="hover"><img id="thumbnail10" src="" class="ytthumbnail"/></a>
            <a href="#" onclick="changeframe(11)" id="popoverData11" rel="popover" data-placement="bottom" data-original-title="" data-trigger="hover"><img id="thumbnail11" src="" class="ytthumbnail"/></a>
            <a href="#" onclick="changeframe(12)" id="popoverData12" rel="popover" data-placement="bottom" data-original-title="" data-trigger="hover"><img id="thumbnail12" src="" class="ytthumbnail"/></a>
            <a href="#" onclick="changeframe(13)" id="popoverData13" rel="popover" data-placement="bottom" data-original-title="" data-trigger="hover"><img id="thumbnail13" src="" class="ytthumbnail"/></a>
            <a href="#" onclick="changeframe(14)" id="popoverData14" rel="popover" data-placement="bottom" data-original-title="" data-trigger="hover"><img id="thumbnail14" src="" class="ytthumbnail"/></a>
            <a href="#" onclick="changeframe(15)" id="popoverData15" rel="popover" data-placement="bottom" data-original-title="" data-trigger="hover"><img id="thumbnail15" src="" class="ytthumbnail"/></a>
            <a href="#" onclick="changeframe(16)" id="popoverData16" rel="popover" data-placement="bottom" data-original-title="" data-trigger="hover"><img id="thumbnail16" src="" class="ytthumbnail"/></a>
            <a href="#" onclick="changeframe(17)" id="popoverData17" rel="popover" data-placement="bottom" data-original-title="" data-trigger="hover"><img id="thumbnail17" src="" class="ytthumbnail"/></a>
            <a href="#" onclick="changeframe(18)" id="popoverData18" rel="popover" data-placement="bottom" data-original-title="" data-trigger="hover"><img id="thumbnail18" src="" class="ytthumbnail"/></a>
            <a href="#" onclick="changeframe(19)" id="popoverData19" rel="popover" data-placement="bottom" data-original-title="" data-trigger="hover"><img id="thumbnail19" src="" class="ytthumbnail"/></a>
            </div>
        </div>--%>
    
    </div>
    </div>

    <div class="right_float"> 
    <%--<h2>Biography <button style="float: right" id="popup" onclick="div_show()">Edit</button></h2><br />--%>
    <h2>Biography<button style="float: right" class="btn btn-default btn-sm"  onclick="window.location='/EditProfile.aspx';"><span class="glyphicon glyphicon-pencil"></span>Edit</button></h2><br />

        <div id="abc" vertical-align="center">
            <div id="popupContact" >
<%--            <form role="form">
                <img id="close" src="http://www.skd.museum/typo3conf/ext/perfectlightboxjquery/res/images/close.gif" onclick ="div_hide()">
                <div class="form-group">
                     <label for="name">Name:</label>
                      <input type="text"  class="form-control" id="name" placeholder="" required>
                </div>

                 <div class="form-group">
                     <label for="genre">Genre:</label>
                      <input type="text" class="form-control" id="genre" placeholder="">
                </div> 
                <div class="form-group">
                     <label for="agent">Agent:</label>
                      <input type="text" class="form-control" id="agent" placeholder="">
                </div>
                <div class="form-group">
                     <label for="phone">Phone:</label>
                      <input type="tel" class="form-control" id="phone" placeholder="">
                </div>

                <div class="form-group">
                   <label for="message" >Bio:</label>
                      
                        <textarea class="form-control" rows="8" name="message"></textarea>
                      
                </div>
                 <asp:Button type="submit" runat="server" Text="Submit" onclick="formsubmit_Click" class="btn btn-default"></asp:Button>

            </form>--%>


            </div>
        </div>
    <div class="inner_left_float">
    
    <img id="profilePic" runat="server"  src=""/>
    </div>
    <div class="inner_right_float">
         <%--This is the edit popup form --%>
        <strong>
      
        <span id="displayName" runat="server">Name: <br /></span>
        <span id="displayGenre" runat="server">Genre: <br /></span>
        <span id="displayAgent" runat="server">Agent: <br /></span>
        <span id="displayPhone" runat="server">Phone: <br /></span></strong>
    </div>
    <div class="text" >
  <p>  <br /><br />
      <strong id="Biography" runat="server">
         Your Biography here......
      </strong>
  </p>
        
    </div>
        
    </div>
    <br />
<div class="footer">
        <span><strong>© Music Portfolio 2015</strong></span>
    </div>


</body>

</html>
