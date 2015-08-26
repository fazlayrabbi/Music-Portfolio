using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Services;
using System.Data;
using System.Data.SqlClient;
using System.Web.Script.Services;
using Skybrud.Social.Instagram;
using Facebook;
using System.Configuration;
using TweetSharp;
using Newtonsoft.Json;

namespace mp
{
    /// <summary>
    /// Summary description for GetBio
    /// </summary>
    [WebService(Namespace = "http://tempuri.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    // To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
    // [System.Web.Script.Services.ScriptService]
    public class GetBio : System.Web.Services.WebService
    {
        public string bio = string.Empty;
        [WebMethod]
        [ScriptMethod(UseHttpGet = true)]
        public string getBio(string user)
        {
            try
            {
                SqlConnection conn = new SqlConnection(System.Configuration.ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString);
                SqlDataReader dr;
                DataTable dt = new DataTable();
                SqlCommand cmd = new SqlCommand();
                cmd.CommandText = "Select userid,displayname,genre,agent,phone,'http://mp.makhdumi.net/images/'+profilepic as profilepic,biography from tbl_musicians (nolock) where userid='" + user + "'";
                cmd.Connection = conn;
                conn.Open();

                if (conn.State == ConnectionState.Open)
                {
                    dr = cmd.ExecuteReader();
                    dt.Load(dr);
                    //dr.Read();
                    if (dt.Rows.Count > 0)
                    {

                        bio = ConvertDataTableTojSonString(dt);
                        //resultset = "DisplayName=" + dr[1].ToString() + ""
                        //img_profilepic.ImageUrl = "~/Images/" + dr[5].ToString();

                        //txt_name.Text = dr[1].ToString();
                        //txt_genre.Text = dr[2].ToString();
                        //txt_agent.Text = dr[3].ToString();
                        //txt_phone.Text = dr[4].ToString();
                        //txt_bio.Text = dr[6].ToString();


                    }
                    dr.Close();
                }
                conn.Close();
            }
            catch (Exception Ex)
            {
                throw Ex;
            }
            return bio;
        }

        public string bioall = string.Empty;
        [WebMethod]
        [ScriptMethod(UseHttpGet = true)]
        public string getBioAll()
        {
            try
            {
                SqlConnection conn = new SqlConnection(System.Configuration.ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString);
                SqlDataReader dr;
                DataTable dt = new DataTable();
                SqlCommand cmd = new SqlCommand();
                cmd.CommandText = "Select userid,displayname,genre,agent,phone,'http://mp.makhdumi.net/images/'+profilepic as profilepic,biography from tbl_musicians (nolock)";
                cmd.Connection = conn;
                conn.Open();

                if (conn.State == ConnectionState.Open)
                {
                    dr = cmd.ExecuteReader();
                    dt.Load(dr);
                    //dr.Read();
                    if (dt.Rows.Count > 0)
                    {

                        bioall = ConvertDataTableTojSonString(dt);
                        //resultset = "DisplayName=" + dr[1].ToString() + ""
                        //img_profilepic.ImageUrl = "~/Images/" + dr[5].ToString();

                        //txt_name.Text = dr[1].ToString();
                        //txt_genre.Text = dr[2].ToString();
                        //txt_agent.Text = dr[3].ToString();
                        //txt_phone.Text = dr[4].ToString();
                        //txt_bio.Text = dr[6].ToString();


                    }
                    dr.Close();
                }
                conn.Close();
            }
            catch (Exception Ex)
            {
                throw Ex;
            }
            return bioall;
        }


        public String ConvertDataTableTojSonString(DataTable dataTable)
        {
            System.Web.Script.Serialization.JavaScriptSerializer serializer =
                   new System.Web.Script.Serialization.JavaScriptSerializer();

            List<Dictionary<String, Object>> tableRows = new List<Dictionary<String, Object>>();

            Dictionary<String, Object> row;

            foreach (DataRow dr in dataTable.Rows)
            {
                row = new Dictionary<String, Object>();
                foreach (DataColumn col in dataTable.Columns)
                {
                    row.Add(col.ColumnName, dr[col]);
                }
                tableRows.Add(row);
            }
            return serializer.Serialize(tableRows);
        }

        public string instagramdata = string.Empty;
        [WebMethod]
        [ScriptMethod(UseHttpGet = true)]
        public string getInstagram(string user)
        {
            try
            {
                SqlConnection conn = new SqlConnection(System.Configuration.ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString);
                SqlDataReader dr;
                SqlCommand cmd = new SqlCommand();
                cmd.CommandText = "Select instagramtoken,instagramid from tbl_ext_creds_instagram (nolock) where userid='" + user + "'";
                cmd.Connection = conn;
                conn.Open();

                if (conn.State == ConnectionState.Open)
                {
                    dr = cmd.ExecuteReader();
                    dr.Read();
                    if (dr.HasRows)
                    {
                        //Session["instagram_token"] = dr[0].ToString();
                        //Session["instagram_id"] = dr[1].ToString();

                        //ScriptManager.RegisterClientScriptBlock(Page, typeof(System.Web.UI.Page), "loadinstagramdata", "getData('" + dr[0].ToString() + "',null,null);", true);
                        InstagramService instagram = InstagramService.CreateFromAccessToken(dr[0].ToString());
                        var recent = instagram.Users.GetRecentMedia();
                        instagramdata = recent.Response.Body.ToString();
                    }
                    //else
                    //{
                    //    Session["instagram_token"] = null;
                    //    Session["instagram_id"] = null;
                    //}
                    dr.Close();
                }
                conn.Close();
            }
            catch (Exception Ex)
            {
                throw Ex;
            }
            //if (instagramdata == string.Empty)
            //    instagramdata = "User not found";
            return instagramdata;
        }

        public string scdata = string.Empty;
        [WebMethod]
        [ScriptMethod(UseHttpGet = true)]
        public string getSoundCloud(string user)
        {
            try
            {
                SqlConnection conn = new SqlConnection(System.Configuration.ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString);
                SqlDataReader dr;
                SqlCommand cmd = new SqlCommand();
                cmd.CommandText = "Select code from tbl_ext_creds_sc (nolock) where userid='" + user + "'";
                cmd.Connection = conn;
                conn.Open();

                if (conn.State == ConnectionState.Open)
                {
                    dr = cmd.ExecuteReader();
                    dr.Read();
                    if (dr.HasRows)
                    {
                        scdata = dr[0].ToString();
                    }
                    //else
                    //{
                    //    Session["instagram_token"] = null;
                    //    Session["instagram_id"] = null;
                    //}
                    dr.Close();
                }
                conn.Close();
            }
            catch (Exception Ex)
            {
                throw Ex;
            }
            //if (instagramdata == string.Empty)
            //    instagramdata = "User not found";
            return scdata;

        }

        public string fbprofile = string.Empty;
        [WebMethod]
        [ScriptMethod(UseHttpGet = true)]
        public string getFBProfile(string user)
        {
            try
            {
                SqlConnection conn = new SqlConnection(System.Configuration.ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString);
                SqlDataReader dr;
                SqlCommand cmd = new SqlCommand();
                cmd.CommandText = "select fb_accesstoken from tbl_ext_creds_fb (nolock) where userid='" + user + "'";
                cmd.Connection = conn;
                conn.Open();

                if (conn.State == ConnectionState.Open)
                {
                    dr = cmd.ExecuteReader();
                    dr.Read();
                    if (dr.HasRows)
                    {
                        var fb = new FacebookClient();
                        fb.AccessToken = dr[0].ToString();
                        dynamic profile = fb.Get("/me");
                        //dynamic posts = fb.Get("/me/posts");
                        string profilepicurl = string.Format("https://graph.facebook.com/{0}/picture", profile.id);
                        fbprofile = profile.ToString();
                    }
                    dr.Close();
                }
                conn.Close();
            }
            catch (Exception Ex)
            {
                throw Ex;
            }
            return fbprofile;
        }


        public string fbposts = string.Empty;
        [WebMethod]
        [ScriptMethod(UseHttpGet = true)]
        public string getFBPosts(string user)
        {
            try
            {
                SqlConnection conn = new SqlConnection(System.Configuration.ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString);
                SqlDataReader dr;
                SqlCommand cmd = new SqlCommand();
                cmd.CommandText = "select fb_accesstoken from tbl_ext_creds_fb (nolock) where userid='" + user + "'";
                cmd.Connection = conn;
                conn.Open();

                if (conn.State == ConnectionState.Open)
                {
                    dr = cmd.ExecuteReader();
                    dr.Read();
                    if (dr.HasRows)
                    {
                        var fb = new FacebookClient();
                        fb.AccessToken = dr[0].ToString();
                        dynamic profile = fb.Get("/me");
                        dynamic posts = fb.Get("/me/posts");
                        //string profilepicurl = string.Format("https://graph.facebook.com/{0}/picture", profile.id);
                        fbposts = posts.ToString();
                    }
                    dr.Close();
                }
                conn.Close();
            }
            catch (Exception Ex)
            {
                throw Ex;
            }
            return fbposts;
        }

        public string twdata = string.Empty;
        class MPtweets
        {
            public string profileImageUrl { get; set; }
            public string ScreenName { get; set; }
            public string CreatedDate { get; set; }
            public string Text { get; set; }
        }
        List<MPtweets> mpt = new List<MPtweets>();
        [WebMethod]
        [ScriptMethod(UseHttpGet = true)]
        public string getTwitter(string user)
        {
            try
            {
                SqlConnection conn = new SqlConnection(System.Configuration.ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString);
                SqlDataReader dr;
                SqlCommand cmd = new SqlCommand();
                cmd.CommandText = "select * from tbl_ext_creds_twitter (nolock) where userid='" + user + "'";
                cmd.Connection = conn;
                conn.Open();

                if (conn.State == ConnectionState.Open)
                {
                    dr = cmd.ExecuteReader();
                    dr.Read();
                    if (dr.HasRows)
                    {
                        //Session["twtoken"] = dr[1].ToString();
                        //Session["twsecret"] = dr[2].ToString();
                        //Session["twscreenname"] = dr[4].ToString();
                        //Session["twuserid"] = dr[3].ToString();

                        var service = new TwitterService(ConfigurationManager.AppSettings["twitter.appkey"].ToString(), ConfigurationManager.AppSettings["twitter.appsecret"].ToString());
                        //service.AuthenticateWith(ConfigurationManager.AppSettings["twitter.accesstoken"].ToString(), ConfigurationManager.AppSettings["twitter.accesstokensecret"].ToString());
                        service.AuthenticateWith(dr[1].ToString(), dr[2].ToString());
                        ListTweetsOnUserTimelineOptions opt = new ListTweetsOnUserTimelineOptions { ScreenName = dr[4].ToString(), Count = 5 };
                        //IEnumerable<TwitterStatus> tweets = service.ListTweetsOnUserTimeline(new ListTweetsOnUserTimelineOptions { ScreenName = Session["twscreenname"].ToString(), Count = 5 });
                        IEnumerable<TwitterStatus> tweets = service.ListTweetsOnUserTimeline(opt);
                        if (tweets != null)
                        {
                            //var t = (TwitterStatus)JsonConvert.DeserializeObject(tweets, typeof(TwitterStatus));


                            foreach (var tweet in tweets)
                            {
                                MPtweets aTweet = new MPtweets();
                                aTweet.profileImageUrl = tweet.Author.ProfileImageUrl;
                                aTweet.ScreenName = tweet.Author.ScreenName;
                                aTweet.CreatedDate = tweet.CreatedDate.ToString();
                                aTweet.Text = tweet.Text;
                                mpt.Add(aTweet);
                                //twitter.InnerHtml = twitter.InnerHtml + "<li> <p> <table><tr><td><img alt=\"\" src=\"" + tweet.Author.ProfileImageUrl + "\" /></td><td> <table> <tr> <td> " + tweet.Author.ScreenName + " </td> </tr> <tr><td>" + tweet.CreatedDate.ToString("MM/dd/yyyy") + " </td> </tr></table> </td></tr></table> </p></li></br>";
                                //twitter.InnerHtml = twitter.InnerHtml + "<li> <p>" + tweet.Text + "</p></li></br>";
                            }

                            twdata = new System.Web.Script.Serialization.JavaScriptSerializer().Serialize(mpt);

                        }


                    }
                    dr.Close();
                }
                conn.Close();
            }
            catch (Exception Ex)
            {
                throw Ex;
            }
            return twdata;
        }

        public string localmedia = string.Empty;
        [WebMethod]
        [ScriptMethod(UseHttpGet = true)]
        public string getLocalMedia(string user)
        {
            try
            {
                SqlConnection conn = new SqlConnection(System.Configuration.ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString);
                SqlDataReader dr;
                DataTable dt = new DataTable();
                SqlCommand cmd = new SqlCommand();
                cmd.CommandText = "Select mediatype,'http://mp.makhdumi.net/uploads/'+filename as filename from tbl_local_media where userid='" + user + "'";
                cmd.Connection = conn;
                conn.Open();

                if (conn.State == ConnectionState.Open)
                {
                    dr = cmd.ExecuteReader();
                    dt.Load(dr);
                    if (dt.Rows.Count > 0)
                        localmedia = ConvertDataTableTojSonString(dt);
                    dr.Close();
                }
                conn.Close();
            }
            catch (Exception Ex)
            {
                throw Ex;
            }
            return localmedia;
        }

    }
}
