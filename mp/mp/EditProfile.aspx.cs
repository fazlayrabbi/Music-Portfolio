using Facebook;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Data.SqlClient;
using System.IO;
using System.IO.Compression;
using System.Linq;
using System.Net;
using System.Security.Cryptography;
using System.Text;
using System.Web;
using System.Web.Script.Serialization;
using System.Web.Services;
using System.Web.UI;
using System.Web.UI.WebControls;


namespace mp
{


    public partial class EditProfile : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!Page.IsPostBack)
            {
                //Session["username"] = "User1";
                if (Session["username"] != null && Session["loggedin"] != null && Session["username"] == Session["loggedin"])
                {
                    getUserBio();
                }
                else
                {
                    Response.Redirect("/Account/Login.aspx");
                    
                }
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
                        img_profilepic.ImageUrl = "~/Images/" + dr[5].ToString();
                        
                        txt_name.Text = dr[1].ToString();
                        txt_genre.Text = dr[2].ToString();
                        txt_agent.Text = dr[3].ToString();
                        txt_phone.Text = dr[4].ToString();
                        txt_bio.Text = dr[6].ToString();
                        

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

        protected void updatepic()
        {

            if (upload_profilepic.HasFile && upload_profilepic.PostedFile != null && upload_profilepic.PostedFile.FileName != "")
            {
                string[] validFileTypes = { "gif", "png", "jpg", "jpeg" };
                string ext = System.IO.Path.GetExtension(upload_profilepic.PostedFile.FileName);
                bool isValidFile = false;
                for (int i = 0; i < validFileTypes.Length; i++)
                {
                    if (ext == "." + validFileTypes[i])
                    {
                        isValidFile = true;
                        break;
                    }
                }
                if (!isValidFile)
                {

                }
                else
                {
                    upload_profilepic.PostedFile.SaveAs(Server.MapPath("images") + "\\" + Session["username"].ToString()+"_"+ upload_profilepic.PostedFile.FileName);
                    updateprofilepic(Session["username"].ToString() + "_" + upload_profilepic.PostedFile.FileName);
                    //img_profilepic.Attributes["src"] = ResolveUrl("images/" + Session["username"].ToString() + "_" + upload_profilepic.PostedFile.FileName);
                }


            }
        }

        protected void updateprofilepic(string filename)
        {
            try
            {
                SqlConnection conn = new SqlConnection(System.Configuration.ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString);
                SqlDataReader dr;
                SqlCommand cmd = new SqlCommand();
                cmd.CommandText = "update tbl_musicians set profilepic='" + filename + "' where userid='" + Session["username"].ToString() +"'";
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

        protected void cmd_save_profile_Click(object sender, EventArgs e)
        {
            try
            {
                SqlConnection conn = new SqlConnection(System.Configuration.ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString);
                SqlDataReader dr;
                SqlCommand cmd = new SqlCommand();
                cmd.CommandText = "exec sp_upd_bio '"+ Session["username"].ToString() + "','" +txt_name.Text + "','"+txt_genre.Text + "','"+txt_agent.Text + "','"+txt_phone.Text + "','"+txt_bio.Text.Replace("'","") + "'";
                cmd.Connection = conn;
                conn.Open();

                if (conn.State == ConnectionState.Open)
                {
                    dr = cmd.ExecuteReader();
                    dr.Read();
                    dr.Close();

                    updatepic();

                }



                conn.Close();
                Response.Redirect("Musician.aspx",false);
            }
            catch (Exception Ex)
            {
                throw Ex;
            }

        }

        protected void cmd_insta_login_Click(object sender, EventArgs e)
        {
            var client_id = ConfigurationManager.AppSettings["instagram.clientid"].ToString();

            var redirect_uri = ConfigurationManager.AppSettings["instagram.redirecturi"].ToString();

            Response.Redirect("https://api.instagram.com/oauth/authorize/?client_id=" + client_id + "&redirect_uri=" + redirect_uri + "&response_type=code");

        }

        protected void cmd_fb_login_Click(object sender, EventArgs e)
        {
            var fb = new FacebookClient();

            var loginUrl = fb.GetLoginUrl(new
            {
                client_id = ConfigurationManager.AppSettings["fb.appid"].ToString(),
                redirect_uri = "http://mp.makhdumi.net/FacebookAuth.aspx",
                response_type = "code",
                scope = "public_profile,email,user_posts,user_photos"

            });
            Response.Redirect(loginUrl.AbsoluteUri);


            //var fb = new FacebookClient();
            //fb.AccessToken = "CAAHCf5OoIDwBACoXn3tMjKc4h8SUmbZCZA2sZCgZCNMxPMu43NgE6iPB6E8GHZCgI9aXkkZBW62NZBHTqoWfAdeEtJT56EuncbGEl4gV3mZCieQRKX7UHgR3zV0QNlD7PIG0hBXxjN5WSw03RPkBXKuDAXvkz4KZAgH60ZCJsincrH08kHwn1LD0xIEWAs866K0hwo2RfY2jQlIj7IZBegdaC6F";
            //dynamic me = fb.Get("/me");
            //if (me == null)
            //    appendlog("me is null");
            //appendlog("me init : " + me.Count.ToString());

        }
        protected void appendlog(string eline)
        {
            //if (System.IO.File.Exists(HttpContext.Current.Request.ApplicationPath + "/Log.txt"))
            //{
            System.IO.StreamWriter Sr = new System.IO.StreamWriter(HttpContext.Current.Server.MapPath(HttpContext.Current.Request.ApplicationPath + "/Log.txt"), true);
            Sr.WriteLine(DateTime.Now.ToString() + " : " + eline);
            Sr.Close();
            //}
        }
        protected void cmd_twitter_login_Click(object sender, EventArgs e)
        {
            OAuthHelper oauthhelper = new OAuthHelper();
            string requestToken = oauthhelper.GetRequestToken();

            if (string.IsNullOrEmpty(oauthhelper.oauth_error))
                Response.Redirect(oauthhelper.GetAuthorizeUrl(requestToken));
            else
                Response.Write(oauthhelper.oauth_error);
        }
        
        [WebMethod]
        public static void save_sc_id(string user, string userid)
        {
            try
            {
                SqlConnection conn = new SqlConnection(System.Configuration.ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString);
                SqlDataReader dr;
                SqlCommand cmd = new SqlCommand();
                cmd.CommandText = "exec sp_ins_sc_creds '" +user+ "','" + userid + "',''";
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

        string mediatype = string.Empty;
        protected void cmd_upload_Click(object sender, EventArgs e)
        {
            txt_up_status.Text = "";
            if (up_local_media.HasFile && up_local_media.PostedFile != null && up_local_media.PostedFile.FileName != "")
            {
                string[] validFileTypes = { "mp3", "mp4" };
                string ext = System.IO.Path.GetExtension(up_local_media.PostedFile.FileName);
                bool isValidFile = false;
                for (int i = 0; i < validFileTypes.Length; i++)
                {
                    if (ext == "." + validFileTypes[i])
                    {
                        isValidFile = true;
                        break;
                    }
                }
                if (!isValidFile)
                {
                    txt_up_status.Text = "Only mp3 and mp4 files are allowed.";
                }
                else
                {
                    string fileprefix = Guid.NewGuid().ToString();

                    up_local_media.PostedFile.SaveAs(Server.MapPath("uploads") + "\\" + fileprefix + up_local_media.PostedFile.FileName);
                    if (ext == ".mp3")
                        mediatype = "audio";
                    else if (ext == ".wmv")
                        mediatype = "video";
                    else if (ext == ".mp4")
                        mediatype = "video";

                    save_media(Session["username"].ToString(), mediatype, fileprefix + up_local_media.PostedFile.FileName);
                    //local.InnerHtml = local.InnerHtml + "<audio src=\"/uploads/" + fileprefix + up_local_media.PostedFile.FileName + "\" controls=\"controls\"></audio><br/>";


                }


            }
        }

        protected void save_media(string user, string type, string filename)
        {
            try
            {
                SqlConnection conn = new SqlConnection(System.Configuration.ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString);
                SqlDataReader dr;
                SqlCommand cmd = new SqlCommand();
                cmd.CommandText = "insert into tbl_local_media values ('" + user + "','" + type + "','" + filename + "')";
                cmd.Connection = conn;
                conn.Open();

                if (conn.State == ConnectionState.Open)
                {
                    dr = cmd.ExecuteReader();
                    dr.Read();
                    dr.Close();
                    txt_up_status.Text = "File has been uploaded.";
                    up_local_media.Dispose();
                }
                conn.Close();
            }
            catch (Exception Ex)
            {
                throw Ex;
            }
        }

        //protected void cmd_twitter_login_Click(object sender, EventArgs e)
        //{
        //    var oAuthConsumerKey = "superSecretKey";
        //    var oAuthConsumerSecret = "superSecretSecret";
        //    var oAuthUrl = "https://api.twitter.com/oauth2/token";
        //    var screenname = "aScreenName";

        //    // Do the Authenticate
        //    var authHeaderFormat = "Basic {0}";

        //    var authHeader = string.Format(authHeaderFormat,
        //        Convert.ToBase64String(Encoding.UTF8.GetBytes(Uri.EscapeDataString(oAuthConsumerKey) + ":" +
        //        Uri.EscapeDataString((oAuthConsumerSecret)))
        //    ));

        //    var postBody = "grant_type=client_credentials";

        //    HttpWebRequest authRequest = (HttpWebRequest)WebRequest.Create(oAuthUrl);
        //    authRequest.Headers.Add("Authorization", authHeader);
        //    authRequest.Method = "POST";
        //    authRequest.ContentType = "application/x-www-form-urlencoded;charset=UTF-8";
        //    authRequest.AutomaticDecompression = DecompressionMethods.GZip | DecompressionMethods.Deflate;

        //    using (Stream stream = authRequest.GetRequestStream())
        //    {
        //        byte[] content = ASCIIEncoding.ASCII.GetBytes(postBody);
        //        stream.Write(content, 0, content.Length);
        //    }

        //    authRequest.Headers.Add("Accept-Encoding", "gzip");

        //    WebResponse authResponse = authRequest.GetResponse();
        //    // deserialize into an object
        //    TwitAuthenticateResponse twitAuthResponse;
        //    using (authResponse)
        //    {
        //        using (var reader = new StreamReader(authResponse.GetResponseStream()))
        //        {
        //            JavaScriptSerializer js = new JavaScriptSerializer();
        //            var objectText = reader.ReadToEnd();
        //            twitAuthResponse = JsonConvert.DeserializeObject<TwitAuthenticateResponse>(objectText);
        //        }
        //    }

        //    // Do the timeline
        //    var timelineFormat = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name={0}&include_rts=1&exclude_replies=1&count=5";
        //    var timelineUrl = string.Format(timelineFormat, screenname);
        //    HttpWebRequest timeLineRequest = (HttpWebRequest)WebRequest.Create(timelineUrl);
        //    var timelineHeaderFormat = "{0} {1}";
        //    timeLineRequest.Headers.Add("Authorization", string.Format(timelineHeaderFormat, twitAuthResponse.token_type, twitAuthResponse.access_token));
        //    timeLineRequest.Method = "Get";
        //    WebResponse timeLineResponse = timeLineRequest.GetResponse();
        //    var timeLineJson = string.Empty;
        //    using (timeLineResponse)
        //    {
        //        using (var reader = new StreamReader(timeLineResponse.GetResponseStream()))
        //        {
        //            timeLineJson = reader.ReadToEnd();
        //        }
        //    }



        //}
        //public class TwitAuthenticateResponse
        //{
        //    public string token_type { get; set; }
        //    public string access_token { get; set; }
        //}
    }
}