using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Data;
using System.Data.SqlClient;
using System.Collections.Specialized;
using System.Configuration;
using System.Net;
using Newtonsoft.Json.Linq;
using Newtonsoft.Json;
using Skybrud.Social.Instagram;
using Skybrud.Social.Instagram.Responses;
using Facebook;
using System.Web.Script.Serialization;
using TweetSharp;


namespace mp
{
    public partial class Default : System.Web.UI.Page
    {
        static string code = string.Empty;
        static string accessCode = string.Empty;

        protected void appendlog(string eline)
        {
            System.IO.StreamWriter Sr = new System.IO.StreamWriter(HttpContext.Current.Server.MapPath(HttpContext.Current.Request.ApplicationPath + "/Log.txt"), true);
            Sr.WriteLine(DateTime.Now.ToString() + " : " + eline);
            Sr.Close();
        }


        protected void Page_Load(object sender, EventArgs e)
        {
            if (!Page.IsPostBack)
            {
                
                //Session["username"] = "saad";
                
                if (Session["username"] == null)
                {
                    Response.Redirect("/Account/Login.aspx",true);
                }
                else
                {
                    view_count(Session["username"].ToString());
                    //Session["username"] = User.Identity.Name;
                    getUserBio();

                    //Process Instagram Auth postback 
                    if (!String.IsNullOrEmpty(Request["code"]) && !Page.IsPostBack)
                    {
                        code = Request["code"].ToString();
                        GetDataInstagramToken();
                    }

                    
                    load_instagram_data();

                    //Process FB Auth postback
                    if (Request.QueryString["fbcode"] != null)
                    {
                        
                        accessCode = Request.QueryString["fbcode"].ToString();
                        save_fb_accesscode(Session["username"].ToString(), accessCode);

                        
                        var fb = new FacebookClient();

                        dynamic result = fb.Post("oauth/access_token", new
                        {

                            client_id = ConfigurationManager.AppSettings["fb.appid"].ToString(),

                            client_secret = ConfigurationManager.AppSettings["fb.secret"].ToString(),

                            redirect_uri = "http://mp.makhdumi.net/FacebookAuth.aspx",

                            code = accessCode

                        });

                        var accessToken = result.access_token;
                        var expires = result.expires;

                        save_fb_token(Session["username"].ToString(), accessToken, expires.ToString());

                        Session["fb_accesstoken"] = accessToken;
                    }


                    load_fb_token();
                    load_fb_data();


                    if (Request.QueryString["twtoken"]!=null)
                    {
                        Session["twtoken"] = Request.QueryString["twtoken"].ToString();
                        Session["twsecret"] = Request.QueryString["twsecret"].ToString();
                        Session["twscreenname"] = Request.QueryString["twscreenname"].ToString();
                        Session["twuserid"] = Request.QueryString["twuserid"].ToString();

                        save_tw_token(Session["username"].ToString(), Session["twtoken"].ToString(), Session["twsecret"].ToString(), Session["twscreenname"].ToString(), Session["twuserid"].ToString());


                    }

                    load_tw_token();
                    load_tw_data();

                    if (Request.QueryString["sc_id"]!=null)
                    {
                        Session["sc_id"] = Request.QueryString["sc_id"].ToString();
                        //Session["sc_accesstoken"] = Request.QueryString["sc_accesstoken"].ToString();
                        save_sc_token(Session["username"].ToString(), Session["sc_id"].ToString(), "");

                    }


                    load_sc_token();

                    load_local_media();
                }

            }
        }

        protected void view_count(string user)
        {
            try
            {
                SqlConnection conn = new SqlConnection(System.Configuration.ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString);
                SqlDataReader dr;
                SqlCommand cmd = new SqlCommand();
                cmd.CommandText = "exec sp_upd_viewcount '" + user + "'";
                cmd.Connection = conn;
                conn.Open();

                if (conn.State == ConnectionState.Open)
                {
                    dr = cmd.ExecuteReader();
                    dr.Read();
                    dr.Close();
                }
                conn.Close();

            }
            catch (Exception Ex)
            {
                throw Ex;
            }
        }

        protected void save_sc_token(string user, string code, string token)
        {
            try
            {
                SqlConnection conn = new SqlConnection(System.Configuration.ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString);
                SqlDataReader dr;
                SqlCommand cmd = new SqlCommand();
                cmd.CommandText = "exec sp_ins_sc_creds '" + user + "','" +code + "','" + token + "'";
                cmd.Connection = conn;
                conn.Open();

                if (conn.State == ConnectionState.Open)
                {
                    dr = cmd.ExecuteReader();
                    dr.Read();
                    dr.Close();
                }
                conn.Close();
            }
            catch (Exception Ex)
            {
                throw Ex;
            }

        }

        protected void load_sc_token()
        {
            try
            {
                SqlConnection conn = new SqlConnection(System.Configuration.ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString);
                SqlDataReader dr;
                SqlCommand cmd = new SqlCommand();
                cmd.CommandText = "select * from tbl_ext_creds_sc (nolock) where userid='" + Session["username"].ToString() + "'";
                cmd.Connection = conn;
                conn.Open();

                if (conn.State == ConnectionState.Open)
                {
                    dr = cmd.ExecuteReader();
                    dr.Read();
                    if (dr.HasRows)
                    {
                        Session["sc_id"] = dr[1].ToString();
                        //ScriptManager.RegisterClientScriptBlock(Page, typeof(System.Web.UI.Page), "loadSCdata", "getData(null,null,'" + dr[1].ToString() + "');", true);
                        soundcloud.InnerHtml = "<iframe id=\"sc-widget\" src=\"https://w.soundcloud.com/player/?url=http://api.soundcloud.com/users/" + dr[1].ToString() + "\" scrolling=\"yes\" frameborder=\"no\"></iframe>";
                    }
                    dr.Close();
                }
                conn.Close();
                
                //ScriptManager.RegisterClientScriptBlock(Page, typeof(System.Web.UI.Page), "loadinstagramdata", "getData('"+dr[0].ToString()+"',null,null);", true);
            }
            catch (Exception Ex)
            {
                throw Ex;
            }
        }

        protected void load_tw_data()
        {
            //Session["twscreenname"] = "makhdumi";
            //if (Session["twtoken"] != null)
            //{
            //string updateStatusURL = "https://api.twitter.com/1.1/statuses/user_timeline.json?screenname=" + Session["twscreenname"].ToString();

            //string outUrl;
            //string OAuthHeaderPOST = OAuthUtility.GetAuthorizationHeaderForPost_OR_QueryParameterForGET(new Uri(updateStatusURL), callbackUrl, httpMethod.POST.ToString(), oauth_consumer_key, oauth_consumer_secret, oauth_access_token, oauth_token_secret, out outUrl);

            //HttpWebRequest request = (HttpWebRequest)WebRequest.Create(outUrl);
            ////request.Method = httpMethod.POST.ToString();
            //request.Headers["Authorization"] = OAuthHeaderPOST;
            //request.Method = "GET";
            //request.GetRequestStream();
            ////byte[] array = Encoding.ASCII.GetBytes(postData);
            ////request.GetRequestStream().Write(array, 0, array.Length);

            if (Session["twscreenname"] != null)
            {

                var service = new TwitterService(ConfigurationManager.AppSettings["twitter.appkey"].ToString(), ConfigurationManager.AppSettings["twitter.appsecret"].ToString());
                //service.AuthenticateWith(ConfigurationManager.AppSettings["twitter.accesstoken"].ToString(), ConfigurationManager.AppSettings["twitter.accesstokensecret"].ToString());
                service.AuthenticateWith(Session["twtoken"].ToString(), Session["twsecret"].ToString());
                ListTweetsOnUserTimelineOptions opt = new ListTweetsOnUserTimelineOptions { ScreenName = Session["twscreenname"].ToString(), Count = 5 };
                //IEnumerable<TwitterStatus> tweets = service.ListTweetsOnUserTimeline(new ListTweetsOnUserTimelineOptions { ScreenName = Session["twscreenname"].ToString(), Count = 5 });
                IEnumerable<TwitterStatus> tweets = service.ListTweetsOnUserTimeline(opt);
                if (tweets != null)
                {
                    foreach (var tweet in tweets)
                    {
                        twitter.InnerHtml = twitter.InnerHtml + "<li><img style=\"clear: both; float: left;\" src=\"" + tweet.Author.ProfileImageUrl + "\"/>";
                        twitter.InnerHtml = twitter.InnerHtml + "<p style=\"float: left;\" class=\"fblike\"><strong>@" + tweet.Author.ScreenName + "</strong><br>" + tweet.CreatedDate.ToString("MM/dd/yyyy") + "</p><br>";
                        twitter.InnerHtml = twitter.InnerHtml + ("<p style=\"clear: both;\"></p>");
                        twitter.InnerHtml = twitter.InnerHtml + "<p>" + tweet.Text + "</p>";
                        twitter.InnerHtml = twitter.InnerHtml + "</li><hr>";
                    }
                }
            }
            //appendlog(tweets.ToString());
            

            //}
        }

        protected void load_tw_token()
        {
            try
            {
                SqlConnection conn = new SqlConnection(System.Configuration.ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString);
                SqlDataReader dr;
                SqlCommand cmd = new SqlCommand();
                cmd.CommandText = "select * from tbl_ext_creds_twitter (nolock) where userid='" + Session["username"].ToString() + "'";
                cmd.Connection = conn;
                conn.Open();

                if (conn.State == ConnectionState.Open)
                {
                    dr = cmd.ExecuteReader();
                    dr.Read();
                    if (dr.HasRows)
                    {
                        Session["twtoken"] = dr[1].ToString();
                        Session["twsecret"] = dr[2].ToString();
                        Session["twscreenname"] = dr[4].ToString();
                        Session["twuserid"] = dr[3].ToString();
                    }
                    dr.Close();
                }
                conn.Close();
            }
            catch (Exception Ex)
            {
                throw Ex;
            }
        }

        protected void save_tw_token(string user, string token, string secret, string screenname, string userid)
        {
            try
            {
                SqlConnection conn = new SqlConnection(System.Configuration.ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString);
                SqlDataReader dr;
                SqlCommand cmd = new SqlCommand();
                cmd.CommandText = "exec sp_ins_twitter_creds '" + user + "','" + token + "','" + secret + "','" + userid + "','" + screenname + "'";
                cmd.Connection = conn;
                conn.Open();

                if (conn.State == ConnectionState.Open)
                {
                    dr = cmd.ExecuteReader();
                    dr.Read();
                    dr.Close();
                }
                conn.Close();
            }
            catch (Exception Ex)
            {
                throw Ex;
            }

        }

        protected void save_fb_accesscode(string user, string accesscode)
        {
            try
            {
                SqlConnection conn = new SqlConnection(System.Configuration.ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString);
                SqlDataReader dr;
                SqlCommand cmd = new SqlCommand();
                cmd.CommandText = "insert into tbl_ext_creds_fb_accesscode values ('" + user + "','" + accesscode + "')";
                cmd.Connection = conn;
                conn.Open();

                if (conn.State == ConnectionState.Open)
                {
                    dr = cmd.ExecuteReader();
                    dr.Read();
                    dr.Close();
                }
                conn.Close();
            }
            catch (Exception Ex)
            {
                throw Ex;
            }
        }
        protected void load_fb_token()
        {
            try
            {
                SqlConnection conn = new SqlConnection(System.Configuration.ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString);
                SqlDataReader dr;
                SqlCommand cmd = new SqlCommand();
                cmd.CommandText = "select fb_accesstoken from tbl_ext_creds_fb (nolock) where userid='" + Session["username"].ToString() + "'";
                cmd.Connection = conn;
                conn.Open();

                if (conn.State == ConnectionState.Open)
                {
                    dr = cmd.ExecuteReader();
                    dr.Read();
                    if (dr.HasRows)
                        Session["fb_accesstoken"] = dr[0].ToString();
                    dr.Close();
                }
                conn.Close();
            }
            catch (Exception Ex)
            {
                throw Ex;
            }
        }

        protected void load_fb_data()
        {
            if (Session["fb_accesstoken"] != null)
            {
                try
                {
                    var fb = new FacebookClient();
                    fb.AccessToken = Session["fb_accesstoken"].ToString();
                    dynamic profile = fb.Get("/me");
                    dynamic posts = fb.Get("/me/posts");
                    String str;
                    dynamic jsonData;
                    dynamic likes= "";
                    dynamic comments= "";
                    string profilepicurl = string.Format("https://graph.facebook.com/{0}/picture", profile.id);





                    for (int i = 0; i < posts.data.Count; i++)
                    {
                        try
                        {
                            jsonData = posts.data[i].likes;
                            likes = jsonData.data.length;
                        }
                        catch (Exception e)
                        {
                        }
                        try
                        {
                            jsonData = posts.data[i].comments;
                            comments = jsonData.data.length;
                        }
                        catch (Exception e) { }

                        

            fbstatus.InnerHtml = fbstatus.InnerHtml +"<li id=\"fblist\">";
        str = "\"" + posts.data[i].story + "\"";
        if ((str.Contains("updated")) || (str.Contains("changed")) || (str.Contains("shared")))
            fbstatus.InnerHtml = fbstatus.InnerHtml +"<strong><p id=\"scrollpara\">" + posts.data[i].story + "</p></strong>";

        fbstatus.InnerHtml = fbstatus.InnerHtml +"<img style=\"clear: both; float: left;\" src='" + profilepicurl + "'/>";
        fbstatus.InnerHtml = fbstatus.InnerHtml + "<p style=\"float: left;\" class=\"fblike\"><strong>" + profile.name + "</strong><br>" + posts.data[i].updated_time + "</p><br>";
       
        fbstatus.InnerHtml = fbstatus.InnerHtml +"<p style=\"clear: both;\"></p>";
       


        if (posts.data[i].message !=null)
            fbstatus.InnerHtml = fbstatus.InnerHtml +"<p id=\"scrollpara\">" + posts.data[i].message + "</p>";
        if ((str.Contains("shared a link") )) {

            if ((posts.data[i].picture !=null)) {
                fbstatus.InnerHtml = fbstatus.InnerHtml +"<a  href='" + posts.data[i].link + "'>";
                fbstatus.InnerHtml = fbstatus.InnerHtml +"<img style=\"clear: both; float: left;\" class=\"fblink\" src='" + posts.data[i].picture + "'/></a>";
            }
            fbstatus.InnerHtml = fbstatus.InnerHtml +"<a target='_blank' href='" + posts.data[i].link + "'><p style=\"float: left;\">";
            if ((posts.data[i].name !=null)) 
                 fbstatus.InnerHtml = fbstatus.InnerHtml +"<strong>" + posts.data[i].name + "</strong><br>";
            if ((posts.data[i].description !=null)) 
                 fbstatus.InnerHtml = fbstatus.InnerHtml + posts.data[i].description + "<br>";

            fbstatus.InnerHtml = fbstatus.InnerHtml +"</p><p style=\"clear: both;\"></p></a>";
        }
        else {
            if ((str.Contains("shared") ) && (str.Contains("shared a link"))) {
                fbstatus.InnerHtml = fbstatus.InnerHtml +"<hr>";
                     fbstatus.InnerHtml = fbstatus.InnerHtml +"<p style=\"float: left;\" class=\"fblike\"><strong>" + posts.data[i].name + "</strong></p><br>";
                fbstatus.InnerHtml = fbstatus.InnerHtml +"<p style=\"clear: both;\"></p>";
                if (posts.data[i].description !=null)
                    fbstatus.InnerHtml = fbstatus.InnerHtml +"<p id=\"scrollpara\">" + posts.data[i].description + "</p>";

            }

            if ((posts.data[i].picture !=null) && (posts.data[i].type != "video")) {

                fbstatus.InnerHtml = fbstatus.InnerHtml +"<img align=\"middle\"  src=\"" + posts.data[i].picture + "\"/>";
            }
            if (posts.data[i].type == "video")
                fbstatus.InnerHtml = fbstatus.InnerHtml +"<video width=\"300\" height=\"200\" controls> <source src='" + posts.data[i].source + "'></video>";


            if ((str.Contains("shared") ) && (str.Contains("shared a link") ))
                fbstatus.InnerHtml = fbstatus.InnerHtml +"<hr>";


        }








        if ((posts.data[i].likes != null) && (posts.data[i].comments != null))
            fbstatus.InnerHtml = fbstatus.InnerHtml +"<p>Likes: " + likes + "  Comments: " + comments + "</p><br>";
        else if ((posts.data[i].likes == null) && (posts.data[i].comments == null))
            fbstatus.InnerHtml = fbstatus.InnerHtml +"<p>Likes: 0  Comments: 0</p><br>";
        else if ((posts.data[i].likes != null) && (posts.data[i].comments == null))
            fbstatus.InnerHtml = fbstatus.InnerHtml +"<p>Likes: " + likes + " Comments: 0</p><br>";
        if ((posts.data[i].likes == null) && (posts.data[i].comments != null))
            fbstatus.InnerHtml = fbstatus.InnerHtml +"<p>Likes: 0  Comments: " + comments + "</p>";

        fbstatus.InnerHtml = fbstatus.InnerHtml + "</li><hr>";

                    }
                    
                }
                catch (Exception e)
                {
                    //throw e;
                }
            
        }}

        protected void save_fb_token(string user, string accesstoken, string exp)
        {
            try
            {
                SqlConnection conn = new SqlConnection(System.Configuration.ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString);
                SqlDataReader dr;
                SqlCommand cmd = new SqlCommand();
                cmd.CommandText = "exec sp_ins_fb_creds '" + user + "','" + accesstoken + "','" + exp + "'";
                cmd.Connection = conn;
                conn.Open();

                if (conn.State == ConnectionState.Open)
                {
                    dr = cmd.ExecuteReader();
                    dr.Read();
                    dr.Close();
                }
                conn.Close();
            }
            catch (Exception Ex)
            {
                throw Ex;
            }
        }
    
        public void GetDataInstagramToken()
        {
            //var json = "";

            try
            {
                NameValueCollection parameters = new NameValueCollection();
                parameters.Add("client_id", ConfigurationManager.AppSettings["instagram.clientid"].ToString());
                parameters.Add("client_secret", ConfigurationManager.AppSettings["instagram.clientsecret"].ToString());
                parameters.Add("grant_type", "authorization_code");
                parameters.Add("redirect_uri", ConfigurationManager.AppSettings["instagram.redirecturi"].ToString());
                parameters.Add("code", code);

                WebClient client = new WebClient();
                var result = client.UploadValues("https://api.instagram.com/oauth/access_token", "POST", parameters);
                var response = System.Text.Encoding.Default.GetString(result);
                // deserializing nested JSON string to object

                var jsResult = (JObject)JsonConvert.DeserializeObject(response);
                string accessToken = (string)jsResult["access_token"];
                int id = (int)jsResult["user"]["id"];

                save_instagramid(Session["username"].ToString(),accessToken, id);

                //This code register id and access token to get on client side

                //Page.ClientScript.RegisterStartupScript(this.GetType(), "GetToken", "<script>var instagramaccessid=\"" + @"" + id + "" + "\"; var instagramaccesstoken=\"" + @"" + accessToken + "" + "\";</script>");

                //lbl_instagram_token.Text  = accessToken;
                //lbl_instagram_id.Text = id.ToString();
            }

            catch (Exception ex)
            {
                throw ex;
            }

        }

        protected void save_instagramid(string user, string accesstoken, int id)
        {
            try
            {
                SqlConnection conn = new SqlConnection(System.Configuration.ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString);
                SqlDataReader dr;
                SqlCommand cmd = new SqlCommand();
                cmd.CommandText = "exec sp_ins_instagram_token'" + user + "','" + accesstoken + "'," + id.ToString();
                cmd.Connection = conn;
                conn.Open();

                if (conn.State == ConnectionState.Open)
                {
                    dr = cmd.ExecuteReader();
                    dr.Read();
                    dr.Close();
                }
                conn.Close();
            }
            catch (Exception Ex)
            {
                throw Ex;
            }

        }

        protected void getUserBio()
        {
            try
            {
                SqlConnection conn = new SqlConnection(System.Configuration.ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString);
                SqlDataReader dr;
                SqlCommand cmd = new SqlCommand();
                cmd.CommandText = "Select userid,displayname,genre,agent,phone,profilepic,biography from tbl_musicians (nolock) where userid='" + Session["username"].ToString() + "'";
                cmd.Connection = conn;
                conn.Open();

                if (conn.State == ConnectionState.Open)
                {
                    dr = cmd.ExecuteReader();
                    dr.Read();
                    if (dr.HasRows)
                    {
                        profilePic.Attributes["src"] = ResolveUrl("http://mp.makhdumi.net/images/" + dr[5].ToString());
                        displayName.InnerHtml  = "Name: " + dr[1].ToString()+"<br/>";
                        displayGenre.InnerHtml = "Genre: " + dr[2].ToString() + "<br/>";
                        displayAgent.InnerHtml = "Agent: " + dr[3].ToString() + "<br/>";
                        displayPhone.InnerHtml = "Phone: " + dr[4].ToString() + "<br/>";

                        Biography.InnerText = dr[6].ToString();
                    }
                    dr.Close();
                }
                conn.Close();
            }
            catch (Exception Ex)
            {
                throw Ex;
            }
        }

        protected void load_instagram_data()
        {
            try
            {
                SqlConnection conn = new SqlConnection(System.Configuration.ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString);
                SqlDataReader dr;
                SqlCommand cmd = new SqlCommand();
                cmd.CommandText = "Select instagramtoken,instagramid from tbl_ext_creds_instagram (nolock) where userid='" + Session["username"].ToString() + "'";
                cmd.Connection = conn;
                conn.Open();

                if (conn.State == ConnectionState.Open)
                {
                    dr = cmd.ExecuteReader();
                    dr.Read();
                    if (dr.HasRows)
                    {
                        Session["instagram_token"] = dr[0].ToString();
                        Session["instagram_id"] = dr[1].ToString();

                        InstagramService instagram = InstagramService.CreateFromAccessToken(Session["instagram_token"].ToString());
                        var recent = instagram.Users.GetRecentMedia();
                        instapics.InnerHtml = "<ul>";
                        foreach (var media in recent.Body.Data)
                        {
                            var value = media;
                            instapics.InnerHtml = instapics.InnerHtml + "<li><a href='" + media.Images.StandardResolution.Url + "'><img class=\"thumbnails\" src='" + media.Images.LowResolution.Url + "'/></a></li><br>";
                                        //$(".instapics").append("<li><a target='_blank' href='" + data.data[i].link + "'><img class=\"thumbnails\"   src='" + data.data[i].images.low_resolution.url + "'></img></a></li><br>");

                        }
                        instapics.InnerHtml = instapics.InnerHtml + "</ul>";


                        //ScriptManager.RegisterClientScriptBlock(this, typeof(System.Web.UI.Page), "renderinstagramdata", "renderData('"+dr[0].ToString()+"',null,null);", true);
                        //ScriptManager.RegisterClientScriptBlock(this.Page, Page.GetType(), "loadinstagramdata", "<script type=\"text/javascript\">alert('hello');</script>", false);
                        //string script = "alert('hello');";
                        //ScriptManager.RegisterClientScriptBlock(this, this.GetType(), "clentscript", script, true);
                    }
                    else
                    {
                        Session["instagram_token"] = null;
                        Session["instagram_id"] = null;
                    }
                    dr.Close();
                }
                conn.Close();
            }
            catch (Exception Ex)
            {
                throw Ex;
            }
 
        }

        protected void updateprofilepic(string filename)
        {
            try
            {
                SqlConnection conn = new SqlConnection(System.Configuration.ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString);
                SqlDataReader dr;
                SqlCommand cmd = new SqlCommand();
                cmd.CommandText = "update tbl_musicians set profilepic='" + filename + "' where userid='" + User.Identity.Name + "'";
                cmd.Connection = conn;
                conn.Open();

                if (conn.State == ConnectionState.Open)
                {
                    dr = cmd.ExecuteReader();
                    dr.Read();
                    dr.Close();
                }
                conn.Close();
            }
            catch (Exception Ex)
            {
                throw Ex;
            }
        }

        //protected void cmd_profilepic_Click(object sender, EventArgs e)
        //{

        //    if (upload_profilepic.HasFile && upload_profilepic.PostedFile != null && upload_profilepic.PostedFile.FileName != "")
        //    {
        //        string[] validFileTypes = { "gif", "png", "jpg", "jpeg" };
        //        string ext = System.IO.Path.GetExtension(upload_profilepic.PostedFile.FileName);
        //        bool isValidFile = false;
        //        for (int i = 0; i < validFileTypes.Length; i++)
        //        {
        //            if (ext == "." + validFileTypes[i])
        //            {
        //                isValidFile = true;
        //                break;
        //            }
        //        }
        //        if (!isValidFile)
        //        {
                    
        //        }
        //        else
        //        {
        //            //upload_profilepic.PostedFile.SaveAs(Server.MapPath("images") + "\\" + upload_profilepic.PostedFile.FileName);
        //            //updateprofilepic(upload_profilepic.PostedFile.FileName);
        //            //img_profilepic.Attributes["src"] = ResolveUrl("images/" + upload_profilepic.PostedFile.FileName);
        //        }


        //    }
        //}

        protected void formsubmit_Click(object sender, EventArgs e)
        {
            appendlog(Request.Form["name"].ToString());
        }

        protected void Fbutton_Click(object sender, EventArgs e)
        {
            Response.Redirect("Musician.aspx?fbcode=AQCpehLMYn9qAckgoIKVcD1EHK1Zc5RNTEpGOD5SAFIl8Wp7r1QKHOytrqRu2saShC9eKJS65JmGx5tTicIZm-gEUc2Juld0eLGiu2Vi8c7uxaWto-eQT3CKq4g7pufnSFjgetWbyW_AQFhHoW_jSbgMYEzT5LmSZHtFf0ELQGCk_JT6vkZ31I2yWXOcBBZXAkZx4t8UvBI6UeaqUsq5rpRl2-3_d0jkd1Y5IaMod5eVcXN9EdNmhqsLaF6LU56hP9lYnATQWfgwghj7pJuYo5eR7yC1o90MSSCH9-5IOVF0xH61Iw7JrlMuYAHoICLEnLE#_=_");

            var fb = new FacebookClient();

            var loginUrl = fb.GetLoginUrl(new
                {
                    client_id = ConfigurationManager.AppSettings["fb.appid"].ToString(),
                    redirect_uri = "http://mp.makhdumi.net/FacebookAuth.aspx",
                    response_type = "code",
                    scope = "user_posts"

                });
            Response.Redirect(loginUrl.AbsoluteUri);

            //var fb = new FacebookClient();
            //fb.AccessToken = "CAAHCf5OoIDwBACoXn3tMjKc4h8SUmbZCZA2sZCgZCNMxPMu43NgE6iPB6E8GHZCgI9aXkkZBW62NZBHTqoWfAdeEtJT56EuncbGEl4gV3mZCieQRKX7UHgR3zV0QNlD7PIG0hBXxjN5WSw03RPkBXKuDAXvkz4KZAgH60ZCJsincrH08kHwn1LD0xIEWAs866K0hwo2RfY2jQlIj7IZBegdaC6F";
            //dynamic me = fb.Get("/me");
            //if (me == null)
            //    appendlog("me is null");
            //appendlog("me init : " + me.Count.ToString());



        }

        protected void invoke_instagram_login(object sender,EventArgs e)
        {
            var client_id = ConfigurationManager.AppSettings["instagram.clientid"].ToString();

            var redirect_uri = ConfigurationManager.AppSettings["instagram.redirecturi"].ToString();

            Response.Redirect("https://api.instagram.com/oauth/authorize/?client_id=" + client_id + "&redirect_uri=" + redirect_uri + "&response_type=code");

        }

        protected void Instabutton_Click(object sender, EventArgs e)
        {
            if (Session["instagram_token"] == null || Session["instagram_id"] == null)
            {
                var client_id = ConfigurationManager.AppSettings["instagram.clientid"].ToString();

                var redirect_uri = ConfigurationManager.AppSettings["instagram.redirecturi"].ToString();

                Response.Redirect("https://api.instagram.com/oauth/authorize/?client_id=" + client_id + "&redirect_uri=" + redirect_uri + "&response_type=code");


            }
            else
            {
                InstagramService instagram = InstagramService.CreateFromAccessToken(Session["instagram_token"].ToString());
                var recent = instagram.Users.GetRecentMedia();
                //local.InnerHtml = "<ul>";
                foreach (var media in recent.Body.Data)
                {
                    //var value = media;
                    //local.InnerHtml  = local.InnerHtml +  "<li><a href='" + media.Images.StandardResolution.Url + "'><img class=\"thumbnails\" src='" + media.Images.LowResolution.Url + "'/></a></li><br>";
                    //            $(".instapics").append("<li><a target='_blank' href='" + data.data[i].link + "'><img class=\"thumbnails\"   src='" + data.data[i].images.low_resolution.url + "'></img></a></li><br>");

                }
                //local.InnerHtml = local.InnerHtml + "</ul>";



            }
        }

        protected void load_local_media()
        {
            try
            {
                SqlConnection conn = new SqlConnection(System.Configuration.ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString);
                SqlDataReader dr;
                SqlCommand cmd = new SqlCommand();
                cmd.CommandText = "Select * from tbl_local_media where userid='" + Session["username"].ToString() + "'";
                cmd.Connection = conn;
                conn.Open();

                if (conn.State == ConnectionState.Open)
                {
                    dr = cmd.ExecuteReader();
                    //dr.Read();
                    while(dr.Read())
                    {
                        if (dr[1].ToString() == "audio")
                            local.InnerHtml = local.InnerHtml + "<audio src=\"/uploads/" + dr[2].ToString() + "\" controls=\"controls\"></audio><br/>";
                        else if (dr[1].ToString() == "video")
                            local.InnerHtml = local.InnerHtml + "<video width=\"320\" height=\"240\" src=\"/uploads/" + dr[2].ToString() + "\" controls=\"controls\"></video><br/>";

                    }
                    dr.Close();
                }
                conn.Close();
            }
            catch (Exception Ex)
            {
                throw Ex;
            }
        }


    }
}