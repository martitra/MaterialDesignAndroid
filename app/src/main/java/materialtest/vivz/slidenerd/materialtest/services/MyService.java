package materialtest.vivz.slidenerd.materialtest.services;

import materialtest.vivz.slidenerd.materialtest.logging.L;
import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;

/**
 * Creado por soft12 el 27/08/2015.
 */
public class MyService extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        L.t(this, "onStartJob");
        //para < lolipop
        jobFinished(jobParameters, false);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
