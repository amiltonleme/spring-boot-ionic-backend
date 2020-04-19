package com.amiltonleme.cursomc.services;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amiltonleme.cursomc.services.exceptions.FileException;


//Serviço responsável por fornecer funcionalidades de imagem
@Service
public class ImageService {
	
	
	/*Esse método recebe um MultipartFile e retorna (converte) um bufferedImage que é um tipo
	de imagem do Java que já estará no formato jpg*/
	public BufferedImage getJpgImageFromFile(MultipartFile uploadedFile) {
		//getOriginalFilename() pega o nome do arquivo
		//FilenameUtils.getExtension pega a extensão do arquivo
		String ext = FilenameUtils.getExtension(uploadedFile.getOriginalFilename());
		if (!"png".equals(ext) && !"jpg".equals(ext)) {
			throw new FileException("Somente imagens PNG e JPG são permitidas");
		}

		try {
			//Tenta obter um BufferedImage a partir do MultipartFile
			BufferedImage img = ImageIO.read(uploadedFile.getInputStream());
			if ("png".equals(ext)) {
				img = pngToJpg(img);
			}
			return img;
		} catch (IOException e) {
			throw new FileException("Erro ao ler arquivo");
		}
	}

	//Utilizada pelo método de cima
	public BufferedImage pngToJpg(BufferedImage img) {
		BufferedImage jpgImage = new BufferedImage(img.getWidth(), img.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		jpgImage.createGraphics().drawImage(img, 0, 0, Color.WHITE, null);
		return jpgImage;
	}

	//Esse método retorna um InputStream, que é um objeto que encapsula a leitura a partir de um BufferedImage
	//Esse método é criado porque é o método que faz o upload pro S3 que recebe um InputStream
	public InputStream getInputStream(BufferedImage img, String extension) {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(img, extension, os);
			return new ByteArrayInputStream(os.toByteArray());
		} catch (IOException e) {
			throw new FileException("Erro ao ler arquivo");
		}
	}

}
