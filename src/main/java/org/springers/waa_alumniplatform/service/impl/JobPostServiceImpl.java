package org.springers.waa_alumniplatform.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springers.waa_alumniplatform.dto.jobPost.NewJobPost;
import org.springers.waa_alumniplatform.entity.Alumni;
import org.springers.waa_alumniplatform.entity.JobPost;
import org.springers.waa_alumniplatform.exception.EntityNotFound;
import org.springers.waa_alumniplatform.repository.JobPostRepo;
import org.springers.waa_alumniplatform.service.AlumniService;
import org.springers.waa_alumniplatform.service.JobPostService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobPostServiceImpl implements JobPostService {
    private final JobPostRepo jobPostRepo;
    private final AlumniService alumniService;
    private final ModelMapper modelMapper;
    @Override
    public NewJobPost persist(NewJobPost newJobPost, int alumni_id) {
        Alumni alumni = alumniService.getAlumniById(alumni_id);
        JobPost jobPost = modelMapper.map(newJobPost, JobPost.class);
        jobPost.setPoster(alumni);
        jobPostRepo.save(jobPost);
        return newJobPost;
    }

    @Override
    public List<JobPost> getAll() {
        return jobPostRepo.findAll();
    }

    @Override
    public JobPost apply(int jobPostId, int alumniId) {
        Alumni alumni = alumniService.getAlumniById(alumniId);
        JobPost jobPost = getJobPostById(jobPostId);
        jobPost.getApplicants().add(alumni);
        return jobPostRepo.save(jobPost);
    }

    @Override
    public JobPost getJobPostById(int jobPostId) {
        JobPost jobPost = jobPostRepo
                .findById(jobPostId)
                .orElseThrow(()-> new EntityNotFound("Job post not found"));
        return jobPost;
    }
}
