using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using IntroSE.Kanban.Backend.BusinessLayer.BoardControl;
using IntroSE.Kanban.Backend.BusinessLayer.TaskControl;
using Moq;
using NUnit.Framework;
using N = NUnit.Framework;
using System.Collections.Generic;

namespace BoardTests
{
    [TestFixture]
    public class MoveColumnLeft
    {

        Board b;
        List<Column> columns;
        Mock<Column> backlog;
        Mock<Column> progress;
        Mock<Column> done;
        List<Mock<Column>> Mockcolumns;

        [SetUp]
        public void Setup()
        {
            b = new Board();
            columns = new List<Column>();
            Mockcolumns = new List<Mock<Column>>();
            backlog = new Mock<Column>();
            progress = new Mock<Column>();
            done = new Mock<Column>();
            SetColumns();
        }
        private void SetColumns()
        {
            b.columns = columns;

            backlog.Object.name = "backlog";
            progress.Object.name = "in progress";
            done.Object.name = "done";

            backlog.Object.ord = 0;
            progress.Object.ord = 1;
            done.Object.ord = 2;

            columns.Add(backlog.Object);
            columns.Add(progress.Object);
            columns.Add(done.Object);

            Mockcolumns.Add(backlog);
            Mockcolumns.Add(progress);
            Mockcolumns.Add(done);

            foreach (Mock<Column> c in Mockcolumns)
            {
                c.Object.host = 1;

            }
        }
        [Test]
        [TestCase(1)]
        [TestCase(2)]
        public void MoveLeft1(int col)//valid value
        {
            //arrange 
            //act
            Exception e = null;
            try
            {
                b.MoveColumnLeft(col);
            }
            catch (Exception e2)
            {
                e = e2;
            }

            //assert        
            N.Assert.IsNull(e, e != null ? e.Message : "need to seccses");
        }
        [Test]
        [TestCase("test")]
        [TestCase("Test")]
        [TestCase("TEST")]
        [TestCase("tesT")]
        public void MoveLeft1(string s)//the user is the host.
        {
            //arrange 
            b.cur = s;
            //act
            Exception e = null;
            try
            {
                b.MoveColumnLeft(1);
            }
            catch (Exception e2)
            {
                e = e2;
            }

            //assert        
            N.Assert.IsNull(e, e != null ? e.Message : "need to seccses");
        }
        [Test]
        [TestCase(null)]
        public void MoveLeft2(string s)//the user is not the host
        {
            //arrange 
            b.cur = s;
            //act
            Exception e = null;
            try
            {
                b.MoveColumnLeft(1);
            }
            catch (Exception e2)
            {
                e = e2;
            }

            //assert        
            N.Assert.IsNotNull(e, "the user is not the host");
            if (e != null)
                N.Assert.AreEqual(e.Message, "System Problem", "not right exeption");
        }
        [Test]
        [TestCase("tes t")]
        [TestCase("asd")]
        [TestCase("T EST")]
        [TestCase("sad")]
        public void MoveLeft4(string s)//the user is not the host
        {
            //arrange 
            b.cur = s;
            //act
            Exception e = null;
            try
            {
                b.MoveColumnLeft(1);
            }
            catch (Exception e2)
            {
                e = e2;
            }

            //assert        
            N.Assert.IsNotNull(e, "the user is not the host");
            if (e != null)
                N.Assert.AreEqual(e.Message, $"ID #{s} is not host and can not do this action.", "not right exeption");
        }
        [Test]
        [TestCase(43)]
        [TestCase(23)]
        [TestCase(15)]
        [TestCase(-1)]
        [TestCase(-5)]
        public void MoveLeft3(int num)//invalid column number
        {
            //arrange 
            //act
            Exception e = null;
            try
            {
                b.MoveColumnLeft(num);
            }
            catch (Exception e2)
            {
                e = e2;
            }

            //assert        
            N.Assert.IsNotNull(e, "invalid column number");
            if (e != null)
                N.Assert.AreEqual(e.Message, "Invalid column number.", "not right exeption");
        }
        [Test]
        [TestCase(1)]
        [TestCase(2)]
        public void MoveLeft4(int col)//check if right column move left
        {
            //arrange 
            bool check=false;
            Mockcolumns[col].Setup(x => x.SetOrd(Mockcolumns[col].Object.ord-1)).Callback(()=> check=true);
            //act
            b.MoveColumnLeft(col);

            //assert       
            N.Assert.IsTrue(check, "the right column not move left");
        }
        [Test]
        [TestCase(1)]
        [TestCase(2)]
        public void MoveLeft5(int col)//check if right column move right
        {
            //arrange 
            bool check = false;
            Mockcolumns[col-1].Setup(x => x.SetOrd(Mockcolumns[col-1].Object.ord + 1)).Callback(() => check = true);
            //act
            b.MoveColumnLeft(col);

            //assert       
            N.Assert.IsTrue(check, "the right column not move right");
        }
    }
}
