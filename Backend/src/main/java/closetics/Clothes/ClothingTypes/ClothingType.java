package closetics.Clothes.ClothingTypes;

import com.fasterxml.jackson.annotation.JsonIgnore;

import closetics.Clothes.Clothing;
import jakarta.persistence.*;

@Entity(name="types")
public class ClothingType{
  
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  int id;

  private String typeName;

  public ClothingType(String typeName){
    this.typeName = typeName;        
  }

  public String GetTypename(){
    return typeName;
  }

  public void SetTypename(String typeName){
    this.typeName = typeName;
  }
}
