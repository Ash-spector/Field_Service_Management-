package com.zidio.keystones.service;

import com.zidio.keystones.dto.CreateCustomerDTO;
import com.zidio.keystones.dto.CreateSiteDTO;
import com.zidio.keystones.dto.CustomerDTO;
import com.zidio.keystones.dto.SiteDTO;
import com.zidio.keystones.entity.Customer;
import com.zidio.keystones.entity.Site;
import com.zidio.keystones.exception.ResourceNotFoundException;
import com.zidio.keystones.repository.CustomerRepository;
import com.zidio.keystones.repository.SiteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final SiteRepository siteRepository;

    public CustomerService(CustomerRepository customerRepository, SiteRepository siteRepository) {
        this.customerRepository = customerRepository;
        this.siteRepository = siteRepository;
    }

    @Transactional(readOnly = true)
    public Page<CustomerDTO> getCustomers(String search, Pageable pageable) {
        return customerRepository.searchCustomers(search, pageable)
                .map(this::mapToCustomerDTO);
    }

    @Transactional(readOnly = true)
    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return mapToCustomerDTO(customer);
    }

    @Transactional
    public CustomerDTO createCustomer(CreateCustomerDTO dto) {
        Customer customer = Customer.builder()
                .name(dto.getName())
                .contactEmail(dto.getContactEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .build();
        Customer saved = customerRepository.save(customer);
        return mapToCustomerDTO(saved);
    }

    @Transactional
    public CustomerDTO updateCustomer(Long id, CreateCustomerDTO dto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        customer.setName(dto.getName());
        customer.setContactEmail(dto.getContactEmail());
        customer.setPhone(dto.getPhone());
        customer.setAddress(dto.getAddress());
        Customer updated = customerRepository.save(customer);
        return mapToCustomerDTO(updated);
    }

    @Transactional(readOnly = true)
    public List<SiteDTO> getSitesByCustomer(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer not found with id: " + customerId);
        }
        return siteRepository.findByCustomerId(customerId).stream()
                .map(this::mapToSiteDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SiteDTO> getAllSites() {
        return siteRepository.findAll().stream()
                .map(this::mapToSiteDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public SiteDTO createSite(CreateSiteDTO dto) {
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + dto.getCustomerId()));
        Site site = Site.builder()
                .customer(customer)
                .name(dto.getName())
                .address(dto.getAddress())
                .contactName(dto.getContactName())
                .contactPhone(dto.getContactPhone())
                .build();
        Site saved = siteRepository.save(site);
        return mapToSiteDTO(saved);
    }

    private CustomerDTO mapToCustomerDTO(Customer customer) {
        List<Site> sites = siteRepository.findByCustomerId(customer.getId());
        return CustomerDTO.builder()
                .id(customer.getId())
                .name(customer.getName())
                .contactEmail(customer.getContactEmail())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .createdAt(customer.getCreatedAt())
                .siteCount(sites.size())
                .build();
    }

    private SiteDTO mapToSiteDTO(Site site) {
        return SiteDTO.builder()
                .id(site.getId())
                .customerId(site.getCustomer().getId())
                .customerName(site.getCustomer().getName())
                .name(site.getName())
                .address(site.getAddress())
                .contactName(site.getContactName())
                .contactPhone(site.getContactPhone())
                .createdAt(site.getCreatedAt())
                .build();
    }
}
