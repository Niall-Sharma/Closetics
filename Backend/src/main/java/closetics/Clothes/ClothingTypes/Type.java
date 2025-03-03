package closetics.Clothes.ClothingTypes;

import com.fasterxml.jackson.annotation.JsonIgnore;

import closetics.Clothes.Clothing;
import jakarta.persistence.*;

@Entity(name="types")
public class Type{
  
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  int id;

  @ManyToOne
  @JsonIgnore
  private Clothing clothing;

  private String typeName;

  public Type(String typeName){
    this.typeName = typeName;    
    
  }
}
