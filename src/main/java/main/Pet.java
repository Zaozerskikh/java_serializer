package main;

import ru.hse.homework4.Exported;

@Exported
public class Pet {
    public Pet() { }
    public Pet(String petName) {
        this.petName = petName;
    }
    private String petName = "sobaka";
}
