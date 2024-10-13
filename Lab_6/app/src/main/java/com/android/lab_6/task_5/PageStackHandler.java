package com.android.lab_6.task_5;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.lab_6.R;

public class PageStackHandler {

    private FragmentManager fragmentManager;
    private int addedPagesCount = 0;
    private int removedPagesCount = 0;

    public PageStackHandler(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void addPage(Fragment fragment) {
        addedPagesCount++;
        fragmentManager.beginTransaction()
                // Заменяем .add() на .replace(), чтобы каждый новый фрагмент заменял старый
                .replace(R.id.fragmentContainer, fragment) // Заменяем в контейнере fragmentContainer
                .addToBackStack(null)
                .commit();
    }

    public void removePage() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            removedPagesCount++;
            fragmentManager.popBackStack();
        }
    }

    public int getAddedPagesCount() {
        return addedPagesCount;
    }

    public int getRemovedPagesCount() {
        return removedPagesCount;
    }
}
