using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace mp
{
    public partial class twitter : System.Web.UI.Page
    {

        protected void appendlog(string eline)
        {
            //if (System.IO.File.Exists(HttpContext.Current.Request.ApplicationPath + "/Log.txt"))
            //{
            System.IO.StreamWriter Sr = new System.IO.StreamWriter(HttpContext.Current.Server.MapPath(HttpContext.Current.Request.ApplicationPath + "/Log.txt"), true);
            Sr.WriteLine(DateTime.Now.ToString() + " : " + eline);
            Sr.Close();
            //}
        }
        protected void Page_Load(object sender, EventArgs e)
        {
            appendlog("Twitter page load started");
            if (Request.QueryString["oauth_token"] != null && Request.QueryString["oauth_verifier"] != null)
            {
                appendlog("variables are not null");
                string oauth_token = Request.QueryString["oauth_token"];
                string oauth_verifier = Request.QueryString["oauth_verifier"];
                //string oauth_token = "57YzJgAAAAAAgK9fAAABTg-PL1s";
                //string oauth_verifier = "FHlw9bvLeqfGuFLG4yh0rFZ26iGZHgs6";

                appendlog("token is : " + oauth_token);
                appendlog("verifier is : " + oauth_verifier);

                OAuthHelper oauthhelper = new OAuthHelper();
                appendlog("helper initialized, going to get token");
                oauthhelper.GetUserTwAccessToken(oauth_token, oauth_verifier);

                appendlog("token is : " + oauthhelper.oauth_access_token);
                

                if (string.IsNullOrEmpty(oauthhelper.oauth_error))
                {
                    Response.Redirect("Musician.aspx?twtoken=" + oauthhelper.oauth_access_token + "&twsecret=" + oauthhelper.oauth_access_token_secret + "&twscreenname=" + oauthhelper.screen_name + "&twuserid=" + oauthhelper.user_id);

                    //appendlog("inside no error block");
                    //Session["twtoken"] = oauthhelper.oauth_access_token;
                    //Session["twsecret"] = oauthhelper.oauth_access_token_secret;
                    //Session["twuserid"] = oauthhelper.user_id;
                    //Session["twname"] = oauthhelper.screen_name;


                    //Response.Write("<b>AccessToken=</b>" + oauthhelper.oauth_access_token);
                    //Response.Write("<br /><b>Access Secret=</b>" + oauthhelper.oauth_access_token_secret);
                    //Response.Write("<br /><b>Screen Name=</b>" + oauthhelper.screen_name);
                    //Response.Write("<br /><b>Twitter User ID=</b>" + oauthhelper.user_id);
                }
                //else
                //    Response.Write(oauthhelper.oauth_error);
            }
        }
    }
    
}