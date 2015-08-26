using Microsoft.Owin;
using Owin;

[assembly: OwinStartupAttribute(typeof(mp.Startup))]
namespace mp
{
    public partial class Startup {
        public void Configuration(IAppBuilder app) {
            ConfigureAuth(app);
        }
    }
}
