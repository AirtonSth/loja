package br.com.alura.loja;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.alura.loja.modelo.Projeto;
import junit.framework.Assert;

public class ProjetoTest {
	
	private HttpServer server;
	private Client client;
	private WebTarget target;
	
	@Before
	public void startServer(){
		server = Servidor.inicializaServidor();
		ClientConfig config = new ClientConfig();
		config.register(new LoggingFilter());
		this.client =  ClientBuilder.newClient(config);
		this.target = client.target("http://localhost:8080");	
	}
	
	@After
	public void stopServer(){
		server.stop();
	}
	@Test
	public void testaQueBuscarUmProjetoTrazOProjetoEsperado(){
		Projeto projeto = target.path("projetos/1").request().get(Projeto.class);
		Assert.assertEquals("Minha loja", projeto.getNome());
	}
	
	@Test
	public void testaInserirProjeto(){
		Projeto projeto = new Projeto(10l, "fiergs", 2017);
		Entity<Projeto> entity = Entity.entity(projeto, MediaType.APPLICATION_XML);
		
		Response response = target.path("/projetos").request().post(entity);
		Assert.assertEquals(201, response.getStatus());
		String location = response.getHeaderString("Location");
		Projeto projetoRetorno = client.target(location).request().get(Projeto.class);
		Assert.assertTrue(projetoRetorno.getNome().contains("fiergs"));
	}
}
