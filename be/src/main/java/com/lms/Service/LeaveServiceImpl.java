package com.lms.service;

import com.lms.dto.Leave;
import com.lms.dto.projection.LeaveProjection;
import com.lms.exception.DuplicateException;
import com.lms.exception.NotFoundByIdException;
import com.lms.repository.LeaveRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class LeaveServiceImpl implements LeaveService{

    @Autowired
    private final LeaveRepository leaveRepository;

    public LeaveServiceImpl(LeaveRepository leaveRepository) {
        this.leaveRepository = leaveRepository;
    }

    @Override
    public Page<LeaveProjection> getAllLeaves(Pageable pageable) {
        return leaveRepository.findAllProjectedBy(pageable);
    }

    @Override
    public Optional<com.lms.models.Leave> findLeaveById(Long id) {
        return leaveRepository.findById(id);
    }

    @Override
    public com.lms.models.Leave createLeave(Leave leave) throws DuplicateException {
        String leaveName = leave.getName();
        com.lms.models.Leave leaveByName = leaveRepository.findLeaveByName(leaveName);
        if (!Objects.isNull(leaveByName)){
            throw new DuplicateException("Leave duplicate with name: " + leaveName);
        }
        ModelMapper modelMapper = new ModelMapper();
        com.lms.models.Leave leaveEntity = modelMapper.map(leave, com.lms.models.Leave.class);
        return leaveRepository.save(leaveEntity);
    }

    @Override
    public com.lms.models.Leave updateLeave(Long id, Leave leave) throws NotFoundByIdException, DuplicateException {
        Optional<com.lms.models.Leave> leaveOptional = leaveRepository.findById(id);
        if (leaveOptional.isEmpty()){
            throw new NotFoundByIdException("Leave find by id not found :" + id);
        }
        String leaveName = leave.getName();
        com.lms.models.Leave leaveByIdNotAndName = leaveRepository.findLeaveByIdNotAndName(id, leaveName);
        if (!Objects.isNull(leaveByIdNotAndName)){
            throw new DuplicateException("Leave duplicate with name: " + leaveName);
        }
        com.lms.models.Leave leaveEntity = leaveOptional.get();
        leaveEntity.setName(leave.getName());
        leaveEntity.setDescription(leave.getDescription());
        return leaveRepository.save(leaveEntity);
    }

    @Override
    public void deleteLeave(Long id) {
            leaveRepository.deleteById(id);
    }
}
