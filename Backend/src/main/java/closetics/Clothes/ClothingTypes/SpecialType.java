package closetics.Clothes.ClothingTypes;

import com.fasterxml.jackson.annotation.JsonIgnore;

import closetics.Clothes.Clothing;
import jakarta.persistence.*;

@Entity(name="special_types")
public class SpecialType{
  
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  int id;
  private String specialTypeName;  

  public SpecialType(String specialTypeName){
    this.specialTypeName = specialTypeName;    
    
  }

  public String GetSpecialTypename(){
    return specialTypeName;
  }

  public void SetSpecialTypename(String specialTypeName){
    this.specialTypeName = specialTypeName;
  }
}
