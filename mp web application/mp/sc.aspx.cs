using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace mp
{
    public partial class sc : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            if (Request.QueryString["code"] != null)
            {
                string accessCode = Request.QueryString["code"].ToString();
                //string accessToken = Request.Url.ToString().Split('#')[1];
                Response.Redirect("Musician.aspx?sc_accesscode=" + accessCode);
            }



        }
    }
}