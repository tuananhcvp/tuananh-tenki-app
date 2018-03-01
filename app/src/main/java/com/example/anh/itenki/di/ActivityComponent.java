package com.example.anh.itenki.di;

import com.example.anh.itenki.activity.MainActivity;
import com.example.anh.itenki.fragment.NewCurrentLocationFragment;

import dagger.Subcomponent;

/**
 * ActivityComponent
 * Activity周りのComponent
 */
@ActivityScope
@Subcomponent(modules = {ActivityModule.class})
public interface ActivityComponent {
//    void inject(MainActivity mainActivity);

    void inject(NewCurrentLocationFragment currentLocationFragment);
}
