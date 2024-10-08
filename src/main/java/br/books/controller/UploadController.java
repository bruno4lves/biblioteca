package br.books.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UploadController {

	private static final String UPLOAD_DIR = "C:\\Users\\Bruno\\Pictures\\";

    @GetMapping("/upload")
    public String showUploadForm() {
        return "upload"; // Retorna o nome do arquivo HTML
    }

    @PostMapping("/upload")
    public String handleFileUpload(MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Por favor, selecione uma imagem.");
            return "redirect:/upload";
        }

        try {
            // Salva a imagem na pasta especificada
            File dest = new File(UPLOAD_DIR + file.getOriginalFilename());
            file.transferTo(dest);

            redirectAttributes.addFlashAttribute("message", "Imagem enviada com sucesso: " + file.getOriginalFilename());
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "Falha ao enviar a imagem: " + e.getMessage());
        }

        return "redirect:/upload";
    }
}