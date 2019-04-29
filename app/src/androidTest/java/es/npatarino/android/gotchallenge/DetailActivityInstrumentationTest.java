package es.npatarino.android.gotchallenge;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DetailActivityInstrumentationTest
{
    @Rule
    public ActivityTestRule rule  = new ActivityTestRule(DetailActivity.class){
        @Override
        protected Intent getActivityIntent() {
            Intent intent = new Intent(InstrumentationRegistry.getContext(), DetailActivity.class);
            intent.putExtra("name","Name test");
            intent.putExtra("description","Description test");
            intent.putExtra("imageUrl","url");
            return intent;
        }
    };

    @Test
    public void viewDetail_success(){
        Log.e("@Test","Performing detail success test");
    }
}
