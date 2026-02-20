package com.example.startahb_chat_service.repository;

import com.example.startahb_chat_service.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}