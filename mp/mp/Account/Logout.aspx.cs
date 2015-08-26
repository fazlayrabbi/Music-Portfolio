using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Principal;
using System.Web;
using System.Web.Security;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace mp.Account
{
    public partial class Logout : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            Session["username"] = null;
            Session["fb_accesstoken"] = null;
            Session["twscreenname"] = null;
            Session["twtoken"] = null;
            Session["twsecret"] = null;
            Session["loggedin"] = null;
            FormsAuthentication.SignOut();
            HttpContext.Current.User = new GenericPrincipal(new GenericIdentity(string.Empty), null);
            
            Response.Redirect("/Account/Login.aspx");
        }
    }
}