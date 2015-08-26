using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace mp
{
    public partial class Default1 : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!Page.IsPostBack)
                load_musician();
        }
        protected void load_musician()
        {
            try
            {
                SqlConnection conn = new SqlConnection(System.Configuration.ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString);
                SqlDataReader dr;
                SqlCommand cmd = new SqlCommand();
                cmd.CommandText = "Select * from vw_musicians Order by ViewCount Desc";
                cmd.Connection = conn;
                conn.Open();

                if (conn.State == ConnectionState.Open)
                {
                    dr = cmd.ExecuteReader();
                    
                    while(dr.Read())
                    {
                        //music.InnerHtml = music.InnerHtml + "<table cellspacing=\"10\"><tr><td> <table><tr><td><input type=\"image\" src=\"" + dr[5].ToString() + "\" height=\"150\" width=\"150\"/></td></tr><tr><td><p align=\"center\"><b>" + dr[1].ToString() +"</b> </p></td></tr></table> </td> <td> <p style=\"font-size:18px\"> "+ dr[6].ToString() + "  </p>  </td>   </tr></table>";
                        music.InnerHtml = music.InnerHtml + "<div style=\"float: left; clear: both;\"><div  style=\"float: left; clear: both;\"><button type=\"submit\" name=\"cmd_img\" value=\"" + dr[0].ToString() + "\" onclick=\"imgclick('" + dr[0].ToString() + "')\" > <img src=\"" + dr[5].ToString() + "\" height=\"150\" width=\"150\"></button><p align=\"center\"><b>" + dr[1].ToString() + "</b> </p></div><div class=\"artistInfo\"> <p style=\"font-size:18px\"><strong>" + dr[6].ToString() + " </strong> </p> </div></div><hr>";
                    
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

        protected void cmd_search_Click(object sender, EventArgs e)
        {
            if (txt_artist.Text != "" || ddl_Genre.Text != "")
            {
                music.InnerHtml = "";
                try
                {
                    SqlConnection conn = new SqlConnection(System.Configuration.ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString);
                    SqlDataReader dr;
                    SqlCommand cmd = new SqlCommand();
                    if (txt_artist.Text != "" && ddl_Genre.Text != "")
                        cmd.CommandText = "Select * from vw_musicians (nolock) where displayname like '%" + txt_artist.Text + "%' and Genre='" + ddl_Genre.Text + "'";
                    else if (txt_artist.Text != "" && ddl_Genre.Text == "")
                        cmd.CommandText = "Select * from vw_musicians (nolock) where displayname like '%" + txt_artist.Text + "%'";
                    else if (txt_artist.Text == "" && ddl_Genre.Text != "")
                        cmd.CommandText = "Select * from vw_musicians (nolock) where Genre='" + ddl_Genre.Text + "'";

                    cmd.Connection = conn;
                    conn.Open();

                    if (conn.State == ConnectionState.Open)
                    {
                        dr = cmd.ExecuteReader();

                        while (dr.Read())
                        {
                            //music.InnerHtml = music.InnerHtml + "<table cellspacing=\"10\"><tr><td> <table><tr><td><input type=\"image\" src=\"" + dr[5].ToString() + "\" height=\"150\" width=\"150\"/></td></tr><tr><td><p align=\"center\"><b>" + dr[1].ToString() +"</b> </p></td></tr></table> </td> <td> <p style=\"font-size:18px\"> "+ dr[6].ToString() + "  </p>  </td>   </tr></table>";
                            music.InnerHtml = music.InnerHtml + "<table ><tr><td> <table><tr><td><button type=\"submit\" name=\"cmd_img\" value=\"" + dr[0].ToString() + "\" onclick=\"imgclick('" + dr[0].ToString() + "')\" > <img src=\"" + dr[5].ToString() + "\" height=\"150\" width=\"150\"></button></td></tr><tr><td><p align=\"center\"><b>" + dr[1].ToString() + "</b> </p></td></tr></table> </td> <td> <p style=\"font-size:18px\"> " + dr[6].ToString() + "  </p>  </td>   </tr></table>";

                        }
                        if (!dr.HasRows)
                        {
                            lbl_result.Text = "No matches were found, please try again.";
                            load_musician();
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
            else
            {
                music.InnerHtml = "";
                load_musician(); 
            }
        }
    }
}