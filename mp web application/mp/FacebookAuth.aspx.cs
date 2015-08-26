using Facebook;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace mp
{
    public partial class FacebookAuth : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
                    if (Request.QueryString["code"] != null)
                    {
                        string accessCode = Request.QueryString["code"].ToString();
                        Response.Redirect("Musician.aspx?fbcode=" + accessCode);

                        
                    }
        }


    }
}