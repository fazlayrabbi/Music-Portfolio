var z = 0;
var list = new Array;
var fblist = new Array;
console.log("loading java file");
$(document).ready(function () {
    var disable = "btn btn-primary disabled";
    var able = "btn btn-primary active";
    $("#FBbutton").click(function () {
        $(this).attr("class", disable);
        $("#Twitterbutton").attr("class", able);
        $("#Soundcloudbutton").attr("class", able);
        $("#Instabutton").attr("class", able);
        $("#Localbutton").attr("class", able);
        $("div#facebook").css('display', 'block');
        $("div#local").css('display', 'none');
        $("div#twitter").css('display', 'none');
        $("div#insta").css('display', 'none');
        $("div#soundcloud").css('display', 'none');
        $("div#youtube").css('display', 'none');
        $("#Youtubebutton").attr("class", able);
    });
    $("#Localbutton").click(function () {
        $("#Localbutton").attr("class", disable);
        $("#Twitterbutton").attr("class", able);
        $("#Soundcloudbutton").attr("class", able);
        $("#Instabutton").attr("class", able);
        $("#FBbutton").attr("class", able);
        $("div#facebook").css('display', 'none');
        $("div#local").css('display', 'block');
        $("div#twitter").css('display', 'none');
        $("div#insta").css('display', 'none');
        $("div#soundcloud").css('display', 'none');
        $("div#youtube").css('display', 'none');
        $("#Youtubebutton").attr("class", able);
    });
    $("#Twitterbutton").click(function () {
        $("#Localbutton").attr("class", able);
        $("#Twitterbutton").attr("class", disable);
        $("#Soundcloudbutton").attr("class", able);
        $("#Instabutton").attr("class", able);
        $("#FBbutton").attr("class", able);
        $("div#facebook").css('display', 'none');
        $("div#local").css('display', 'none');
        $("div#twitter").css('display', 'block');
        $("div#insta").css('display', 'none');
        $("div#soundcloud").css('display', 'none');
        $("div#youtube").css('display', 'none');
        $("#Youtubebutton").attr("class", able);
    });
    $("#Instabutton").click(function () {
        $("#Localbutton").attr("class", able);
        $("#Twitterbutton").attr("class", able);
        $("#Soundcloudbutton").attr("class", able);
        $("#Instabutton").attr("class", disable);
        $("#FBbutton").attr("class", able);
        $("div#facebook").css('display', 'none');
        $("div#local").css('display', 'none');
        $("div#twitter").css('display', 'none');
        $("div#insta").css('display', 'block');
        $("div#soundcloud").css('display', 'none');
        $("div#youtube").css('display', 'none');
        $("#Youtubebutton").attr("class", able);
        console.log("insta button click");
        //getData(null, null);

    });
    $("#Soundcloudbutton").click(function () {
        $("#Localbutton").attr("class", able);
        $("#Twitterbutton").attr("class", able);
        $("#Soundcloudbutton").attr("class", disable);
        $("#Instabutton").attr("class", able);
        $("#FBbutton").attr("class", able);
        $("div#facebook").css('display', 'none');
        $("div#local").css('display', 'none');
        $("div#twitter").css('display', 'none');
        $("div#insta").css('display', 'none');
        $("div#soundcloud").css('display', 'block');
        $("div#youtube").css('display', 'none');
        $("#Youtubebutton").attr("class", able);
    });
    $("#Youtubebutton").click(function () {
        $("#Localbutton").attr("class", able);
        $("#Twitterbutton").attr("class", able);
        $("#Soundcloudbutton").attr("class", able);
        $("#Instabutton").attr("class", able);
        $("#FBbutton").attr("class", able);
        $("div#facebook").css('display', 'none');
        $("div#local").css('display', 'none');
        $("div#twitter").css('display', 'none');
        $("div#insta").css('display', 'none');
        $("div#soundcloud").css('display', 'none');
        $("div#youtube").css('display', 'block');
        $("#Youtubebutton").attr("class", disable);
    });
    $("#instaLoad").click(function () {
        var v = z;
        for (; z < v + 6 ; z++) {
            try {
                $(".instapics").append("<li><a target='_blank' href='" + list[z].link + "'><img class=\"thumbnails\"   src='" + list[z].images.low_resolution.url + "'></img></a></li><br>");
            } catch (err) {
                continue;
            }
        }

    });


});
function getInstaData(token) {
    alert(token);
    $.ajax({
        type: "GET",
        dataType: "jsonp",
        cache: false,
        url: "https://api.instagram.com/v1/users/self/media/recent/?access_token=".concat(token),
        success: function (data) {
            for (var i = 0; i < 6; i++) {
                $(".instapics").append("<li><a target='_blank' href='" + data.data[i].link + "'><img class=\"thumbnails\"   src='" + data.data[i].images.low_resolution.url + "'></img></a></li><br>");
                z++;
            }
            for (var i = 0; i < data.data.length; i++) {
                list.push(data.data[i]);

            }
        }
    });
}


//Function To Display Popup
function div_show() {
    document.getElementById('abc').style.display = "block";
}
//Function to Hide Popup
function div_hide() {
    document.getElementById('abc').style.display = "none";
}

//function getData(a, b) {
//    if (a != null)
//        getInstaData(a);//loads Instagram api
//    if (b != null)
//        getYoutubeData(b);// loads youtube api
//}


function renderData(a, b, c) {
    if (a != null)
    {
        console.log("inside renderdata");
        getInstaData(a);//loads Instagram api
    }
       
    if (b != null)
        getYoutubeData(b);// loads youtube api
    if (c != null) {
        
        appendSC(c);// loads SoundCloud data
    }
}



function appendFB(posts) 
{
    
    //console.log(posts.data.len());
    var jsonData = posts.data[0].likes;
    //var jsonData = JSON.Parse(response);
    //console.log(jsonData.data.length);
    //  console.log(jsonData.data.length);
    var str;
    var jsonData;
    var likes;
    var comments;

    $(".fbstatus").append("<p>" + posts.data[0].message + "</p><br>");
    $(".fbstatus").append("<p>" + posts.data[1].message + "</p><br>");
    for (var i = 0; i < posts.data.length; i++) {


        try {
            jsonData = posts.data[i].likes;
            likes = jsonData.data.length;
        }
        catch (err) {
        }
        try {
            jsonData = posts.data[i].comments;
            comments = jsonData.data.length;
        }
        catch (err) { }




        $(".fbstatus").append("<li>");
        str = "\"" + response.data[i].story + "\"";
        if ((str.indexOf("updated") >= 0) || (str.indexOf("changed") >= 0) || (str.indexOf("shared") >= 0))
            $(".fbstatus").append("<strong><p>" + response.data[i].story + "</p></strong><br>");
        if (response.data[i].message !== undefined)
            $(".fbstatus").append("<p>" + response.data[i].message + "</p><br>");

        if (response.data[i].picture !== undefined)
            $(".fbstatus").append("<img src='" + response.data[i].picture + "'/><br>");






        if ((response.data[i].likes !== undefined) && (response.data[i].comments !== undefined))
            $(".fbstatus").append("<p>Likes: " + likes + "  Comments: " + comments + "</p><br>");
        else if ((response.data[i].likes === undefined) && (response.data[i].comments === undefined))
            $(".fbstatus").append("<p>Likes: 0  Comments: 0</p><br>");
        else if ((response.data[i].likes !== undefined) && (response.data[i].comments === undefined))
            $(".fbstatus").append("<p>Likes: " + likes + " Comments: 0</p><br>");
        if ((response.data[i].likes === undefined) && (response.data[i].comments !== undefined))
            $(".fbstatus").append("<p>Likes: 0  Comments: " + comments + "</p>");






        $(".fbstatus").append("</li><hr>");

    }



}

function appendSC(userid) {
    alert(userid);
    SC.oEmbed("http://soundcloud.com/" + userid, { color: "ff0066" }, document.getElementById("soundcloud"));

}