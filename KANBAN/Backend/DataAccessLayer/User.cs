using IntroSE.Kanban.Backend.DataAccessLayer.DALControllers;
using System;

namespace IntroSE.Kanban.Backend.DataAccessLayer
{
    internal class User : DalObject<User>
    {
        public const string UIDAtt = DB._uidcolumn;
        public const string EmailAtt = DB._emailcolumn;
        public const string passwordAtt = DB._passwordcolumn;
        public const string nicknameAtt = DB._nicknamecolumn;
        public const string emailHostAtt = DB._hostcolumn;

        public string password;
        public string Nickname { get; }
        public long EmailHost { get; }
        public long UID { get; }
        public User(long UID, string email, string password, string nickname, long emailHost) : base(new UserCtrl())
        {
            Email = email;
            this.password = password;
            this.Nickname = nickname;
            this.EmailHost = emailHost;
            this.UID = UID;
        }
        protected override string MakeFilter() //make a filter for specific user
        {
            return $"WHERE {EmailAtt}='{Email}'";
        }
        public void Insert()
        {
            if (!controller.Insert(this))
            {
                log.Error("fail to save user for email " + Email);
                throw new Exception("fail to save user for email " + Email);
            }
        }
        public void UpdateHost(long host)
        {
            if (!controller.Update(MakeFilter(), emailHostAtt, host))
            {
                log.Error("failed to update user host " + host + " on email " + Email);
                throw new Exception("failed to update the ordinal for column " + 8 + " on email " + Email);
            }
        }
    }
}
