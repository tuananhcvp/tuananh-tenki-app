package com.example.anh.itenki.di;

import com.example.anh.itenki.activity.MainActivity;
import com.example.anh.itenki.fragment.NewCurrentLocationFragment;

import dagger.Component;

/**
 * ActivityComponent
 * Activity周りのComponent
 */
@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = {ApplicationModule.class, ActivityModule.class})
public interface ActivityComponent {
    void inject(MainActivity mainActivity);

    void inject(NewCurrentLocationFragment currentLocationFragment);
}
