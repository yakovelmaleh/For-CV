using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using log4net.Appender;
using log4net.Layout;


namespace IntroSE.Kanban.Backend.ServiceLayer.Logger
{
    class LogConf
    {
        static string BASE_PATH="myapp.log";
        public static void setup() {
            PatternLayout patternLayout = new PatternLayout();
            patternLayout.ConversionPattern= "%d{yyyy-MM-dd HH:mm:ss} %level - (%type: %method - %line)%newline %message%newline%exception";
            patternLayout.ActivateOptions();

            //Creates and defines a RollingFileAppender for file logging.
            RollingFileAppender roller = new RollingFileAppender();
            roller.AppendToFile = true;
            roller.File = BASE_PATH;
            roller.Layout = patternLayout;
            roller.MaxSizeRollBackups = 20;
            roller.MaximumFileSize = "10MB";
            roller.RollingStyle = RollingFileAppender.RollingMode.Once;
            roller.StaticLogFileName = true;

            //Sets the RollingFileAppender as the logger appender.
            log4net.Config.BasicConfigurator.Configure(roller);
            roller.ActivateOptions();
        }
    }
}
