package Tests;
 class Animal {
    public Animal() { eat(this); }
    public void eat(Animal animal) { System.out.println("Animal.eat(Animal)");}
}
 class Mammal extends Animal {
    public Mammal() {
        eat(this);
    }
     public void eat(Mammal mammal) { System.out.println("Mammal.f(Mammal)"); }

 }
 class TRex extends Mammal {
    public void eat(Animal animal) { System.out.println("TRex.eat(Animal)"); }
    public void eat(Mammal mammal) { System.out.println("TRex.eat(Mammal)"); }
    public void eat(TRex TRex) { System.out.println("TRex.eat(TRex)"); }
}

public class asd {
    public static void main(String[] args) {
        Animal a=new TRex();
        Mammal m=new Mammal();
        a.eat(m);
    }
}
