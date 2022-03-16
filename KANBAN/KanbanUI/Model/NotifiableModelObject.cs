﻿namespace KanbanUI.Model
{

    public class NotifiableModelObject : NotifiableObject
    {
        public BackendController Controller { get; private set; }
        protected NotifiableModelObject(BackendController controller)
        {
            this.Controller = controller;
        }
    }

}
