package closetics.Clothes.ClothingTypes;

import jakarta.persistence.*;

@Entity(name="special_type_table")
public class SpecialType{
  
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  int specialTypeId;

  @Enumerated(EnumType.STRING)
  SPECIALTYPES specialType;

  public SpecialType(){
    
    
  }
}
