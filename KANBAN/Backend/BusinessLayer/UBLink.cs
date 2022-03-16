namespace IntroSE.Kanban.Backend.BusinessLayer
{
    public class UBlink // simple User-Board data link to prevent parameter passing in service layer
    {
        public bool Load { get; set; }
        public string Lastemail { get; set; }
        public int LastId { get; set; }
        public int HostId { get; set; }
        public UBlink()
        {
            Lastemail = null;
            LastId = -1;
            HostId = -1;
            Load = false;
        }
    }
}
