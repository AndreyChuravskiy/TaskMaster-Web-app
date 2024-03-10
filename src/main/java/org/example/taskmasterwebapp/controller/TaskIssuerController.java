package org.example.taskmasterwebapp.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasAuthority('ISSUER')")
public class TaskIssuerController {



}
