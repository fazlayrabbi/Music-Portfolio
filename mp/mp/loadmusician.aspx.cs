using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace mp
{
    public partial class loadmusician : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                 if (Request.QueryString["userid"] != null)
                 {
                     Session["username"] = Request.QueryString["userid"];
                     Response.Redirect("Musician.aspx");

                 }
            }
        }
    }
}