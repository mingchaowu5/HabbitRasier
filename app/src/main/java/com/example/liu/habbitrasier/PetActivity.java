package com.example.liu.habbitrasier;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class PetActivity extends AppCompatActivity {
    private RadioGroup RgGroup;
    PetDataHelper petdb;
    private Pet Data;
    private TextView healthy;
    private TextView food;
    private Button feedButton;
    private Button resetButton;
    private int happyLev = 9;
    private int norLev = 5;
    private int maxHealth = 10;
    private int minHealth = 0;
    private int healthDeduct = 1;
    private int healthPlus = 3;
    private int foodUnit = 1;

    /**
     * health += healthPlus   <= healthScale
     * food --
     **/
    public void feed() {

        petdb = new PetDataHelper(PetActivity.this);
        Pet pet = petdb.getPet("pet");

        if (Integer.valueOf(pet.getFood()) <= 0) return;

        isHealthPlus(true);
        isFoodPlus(false);
        refresh(true);
//        refresh(false);
    }

    /**
     * finish task and add food
     */
    public void finishTask() {
        isFoodPlus(true);
    }

    /**
     * day pass and health deduct
     **/
    public void dayPass() {
        isHealthPlus(false);
    }

    /**
     * Test only
     * Reset the heath and food
     */
    public void reset() {
        petdb = new PetDataHelper(PetActivity.this);
        petdb.updateFood("2", "pet");
        petdb.updateHealthy("4", "pet");
        refresh(false);
    }

    /**
     * food ++/--
     **/
    public void isFoodPlus(boolean add) {
        petdb = new PetDataHelper(PetActivity.this);
        Pet pet = petdb.getPet("pet");

        if (add) {
            petdb.updateFood(String.valueOf(Integer.valueOf(pet.getFood()) + foodUnit), "pet");
        } else {
            petdb.updateFood(String.valueOf(Integer.valueOf(pet.getFood()) - foodUnit), "pet");
        }
    }

    public void isHealthPlus(boolean add) {
        petdb = new PetDataHelper(PetActivity.this);
        Pet pet = petdb.getPet("pet");

        if (add) {
            int maxHealthy = Math.min(Integer.valueOf(pet.getHealthy()) + healthPlus, maxHealth);
            petdb.updateHealthy(String.valueOf(maxHealthy), "pet");
        } else {
            int minHealthy = Math.max(Integer.valueOf(pet.getHealthy()) - healthDeduct, minHealth);
            petdb.updateHealthy(String.valueOf(minHealthy), "pet");
        }
        refresh(!add);
    }

    /**
     * refresh the pet page based on updated data
     * <p>
     * Boolean eat and healthLev define which animation should be play
     */
    public void refresh(boolean eat) {
        petdb = new PetDataHelper(PetActivity.this);
        Data = petdb.getPet("pet");

        healthy = (TextView) findViewById(R.id.petHealthy);
        healthy.setText(Data.getHealthy() + "/10");
        int healthyLev = Integer.valueOf(Data.getHealthy());

        food = (TextView) findViewById(R.id.petFood);
        food.setText(Data.getFood());

        GifImageView gifImageView = findViewById(R.id.image);

//        System.out.println("----------------new healthey is " + Data.getHealthy());
//        int index = Integer.valueOf(Data.getHealthy()) > 9 ?  2:1;

        try {
            GifDrawable gifDrawable;
            if (eat) {
                gifDrawable = new GifDrawable(getResources(), R.drawable.cc_fish);
            } else {
                if (healthyLev >= happyLev) {
                    gifDrawable = new GifDrawable(getResources(), R.drawable.cc_happy);
                } else if (healthyLev >= norLev) {
                    gifDrawable = new GifDrawable(getResources(), R.drawable.cc_running);
                } else {
                    gifDrawable = new GifDrawable(getResources(), R.drawable.cc_upset);
                }
            }
            gifImageView.setImageDrawable(gifDrawable);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);
        RgGroup = (RadioGroup) findViewById(R.id.rg_group);
        feedButton = (Button) findViewById(R.id.feedButton);
        resetButton = (Button) findViewById(R.id.resetButton);

        refresh(false);

//        petdb = new PetDataHelper(PetActivity.this);
//        Data = petdb.getPet("pet");
//
//        healthy = (TextView) findViewById(R.id.petHealthy);
//        healthy.setText("Healthy: " + Data.getHealthy() + "/10");
//
//        food = (TextView) findViewById(R.id.petFood);
//        food.setText("Food: " + Data.getFood());
//
//        GifImageView gifImageView = findViewById(R.id.image);
//        try {
//            GifDrawable gifDrawable = new GifDrawable(getResources(), R.drawable.cc_happy);
//            gifImageView.setImageDrawable(gifDrawable);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        feedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feed();
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });

        RgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.calender:
                        Intent Cal = new Intent(PetActivity.this, Calender.class);
                        startActivity(Cal);
                        break;
                    case R.id.home:
                        Intent Home = new Intent(PetActivity.this, MainActivity.class);
                        startActivity(Home);
                        break;
                    case R.id.achievement:
                        Intent Ach = new Intent(PetActivity.this, Achievement.class);
                        startActivity(Ach);
                        break;
                    default:
                        break;
                }
            }
        });
    }

}
