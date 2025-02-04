/*
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
package org.springframework.samples.petclinic.pets;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.style.ToStringCreator;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Owners;

import java.util.Optional;

@Entity
@Table(name = "pets")
public class Pet {
    @Autowired
    OwnerRepository ownersRepository;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @NotFound(action = NotFoundAction.IGNORE)
    private Integer id;

    @Column(name = "pet_name")
    @NotFound(action = NotFoundAction.IGNORE)
    private String petName;

    @Column(name = "breed")
    @NotFound(action = NotFoundAction.IGNORE)
    private String breed;

    @Column(name = "color")
    @NotFound(action = NotFoundAction.IGNORE)
    private String color;

    @Column(name = "age")
    @NotFound(action = NotFoundAction.IGNORE)
    private String age;

    @Column(name = "owner_name")
    @NotFound(action = NotFoundAction.IGNORE)
    private String owner;

    public Pet(){

    }

    public Pet(int id, String petName, String breed, String color, String age, String ownerName){
        this.id = id;
        this.petName = petName;
        this.breed = breed;
        this.color = color;
        this.age = age;
        owner = ownerName;

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

    public String getOwnerName() {
        return owner;
    }

    public void setOwnerName(String owner) {
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
                .append("owner name ", this.getOwnerName()).toString();
    }
}
