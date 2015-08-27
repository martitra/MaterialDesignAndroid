package materialtest.vivz.slidenerd.materialtest.services;

import java.util.ArrayList;

import materialtest.vivz.slidenerd.materialtest.callbacks.BoxOfficeMoviesLoadedListener;
import materialtest.vivz.slidenerd.materialtest.logging.L;
import materialtest.vivz.slidenerd.materialtest.pojo.Movie;
import materialtest.vivz.slidenerd.materialtest.task.TaskLoadMoviesBoxOffice;
import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;

/**
 * Creado por soft12 el 27/08/2015.
 */
public class MyService extends JobService implements BoxOfficeMoviesLoadedListener {

    private JobParameters jobParameters;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        L.t(this, "onStartJob");
        //para < lolipop
        //jobFinished(jobParameters, false);
        this.jobParameters = jobParameters;
        //new MyTask(this).execute(jobParameters);
        new TaskLoadMoviesBoxOffice(this).execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        L.t(this, "onStopJob");
        return false;
    }

    @Override
    public void onBoxOfficeMoviesLoaded(ArrayList<Movie> listMovies) {
        L.t(this, "onBoxOfficeMoviesLoaded");
        jobFinished(jobParameters, false);
    }

}
