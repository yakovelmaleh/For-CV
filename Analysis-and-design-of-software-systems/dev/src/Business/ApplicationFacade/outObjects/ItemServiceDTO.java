package Business.ApplicationFacade.outObjects;

import java.util.Objects;

public class ItemServiceDTO {

    private long id;
    private String name;

    @Override
    public String toString() {
        return "Item :\tid=" + id +
                "\t\tName='" + name;
    }

    public ItemServiceDTO(long id, String name){
        this.id=id;
        this.name=name;
    }

    public void setName(String name) { this.name = name; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public long getId() { return id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemServiceDTO that = (ItemServiceDTO) o;
        return id == that.id &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
