using System;
using System.IO;
using DAL = IntroSE.Kanban.Backend.DataAccessLayer;

namespace IntroSE.Kanban.Backend.BusinessLayer
{
    class DB
    {
        private static readonly log4net.ILog log = log4net.LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);
        private readonly DAL.DB db;
        public DB()
        {
            db = new DAL.DB();
        }

        public void DBexist() // make sure DB exists during startup
        {

            try
            {
                if (!Sqlfilexist())
                {
                    log.Debug("create SQL file");
                    db.Build();
                }
            }
            catch (Exception e)
            {
                log.Error("fail to create SQL file   " + Sqlfilexist());
                throw new Exception("fail to create SQL file: " + e.Message);
            }
        }
        private bool Sqlfilexist()
        {
            string check = Path.Combine(Directory.GetCurrentDirectory(), DAL.DB._databasename);
            return File.Exists(check);
        }
        public void DropAll()
        {
            db.DropAll();
        }
    }
}
