package com.example.startahb_chat_service.controller;

import com.example.startahb_chat_service.entity.Ticket;
import com.example.startahb_chat_service.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketRepository ticketRepository;

    @PostMapping
    public Ticket create(@RequestBody Ticket ticket) {
        ticket.setCreatedAt(LocalDateTime.now());
        return ticketRepository.save(ticket);
    }

    @GetMapping("/{id}")
    public Ticket get(@PathVariable Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }
}
