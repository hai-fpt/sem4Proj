package com.lms.service;

import com.lms.dto.LeaveDTO;
import com.lms.exception.NotFoundByIdException;
import com.lms.models.Leave;
import com.lms.repository.LeaveRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class LeaveServiceImpl implements LeaveService{

    private final LeaveRepository leaveRepository;

    @Autowired
    public LeaveServiceImpl(LeaveRepository leaveRepository) {
        this.leaveRepository = leaveRepository;
    }

    @Override
    public Page<Leave> getAllLeaves(Pageable pageable) {
        return leaveRepository.findAll(pageable);
    }

    @Override
    public Optional<Leave> findLeaveById(Long id) {
        return leaveRepository.findById(id);
    }

    @Override
    public Leave createLeave(LeaveDTO leave) {
        ModelMapper modelMapper = new ModelMapper();
        Leave leaveEntity = modelMapper.map(leave, Leave.class);
        return leaveRepository.save(leaveEntity);
    }

    @Override
    public Leave updateLeave(Long id, LeaveDTO leave) throws NotFoundByIdException {
        Optional<Leave> leaveOptional = leaveRepository.findById(id);
        if (leaveOptional.isEmpty()){
            throw new NotFoundByIdException("Leave find by id not found ");
        }
        Leave leaveEntity = leaveOptional.get();
        leaveEntity.setName(leave.getName());
        leaveEntity.setDescription(leave.getDescription());
        return leaveRepository.save(leaveEntity);
    }

    @Override
    public void deleteLeave(Long id) {
            leaveRepository.deleteById(id);
    }
}
