package ma.s2m.nxp.fe.settings.services;

import ma.s2m.nxp.fe.settings.domain.installment.CustomerInstallment;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

public interface ICustomerInstallmentService {

    Page<CustomerInstallment> getAllInstallments(Pageable pageable, String customerName, String customerEmail,
                                                 Long customerId);

    Optional<CustomerInstallment> getInstallmentById(Long id);

    CustomerInstallment createInstallment(CustomerInstallment installment, Long customerId) throws BusinessException;

    CustomerInstallment updateInstallment(Long id, CustomerInstallment newData, Long customerId) throws BusinessException;

    void deleteInstallment(Long id) throws BusinessException;
}