package coms309.pets;/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import coms309.people.Person;
import org.springframework.core.style.ToStringCreator;


public class Pet {


    private Integer id;

    private String petName;

    private String breed;


    private String color;

    private String age;

    private Person owner;

    public Pet(){

    }

    public Pet(int id, String petName, String breed, String color, String age, Person owner){
        this.id = id;
        this.petName = petName;
        this.breed = breed;
        this.color = color;
        this.age = age;
        this.owner = owner;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("id", this.getId())
                .append("name", this.getPetName())
                .append("age", this.getAge())
                .append("breed", this.getBreed())
                .append("color", this.getColor())
                .append("owner ", this.getOwner()).toString();
    }
}
