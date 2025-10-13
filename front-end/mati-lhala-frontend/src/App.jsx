import React, { useState, useEffect } from 'react';
import { Users, Droplets, FileText, DollarSign, AlertTriangle, LogOut, UserCircle } from 'lucide-react';

const API_BASE = 'http://localhost:8086/api';

// Componente principal
export default function App() {
  const [user, setUser] = useState(null);
  const [activeTab, setActiveTab] = useState('utentes');
  
  if (!user) {
    return <LoginScreen onLogin={setUser} />;
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="bg-blue-600 text-white shadow-lg">
        <div className="container mx-auto px-4 py-4 flex justify-between items-center">
          <div className="flex items-center gap-2">
            <Droplets size={32} />
            <h1 className="text-2xl font-bold">Mati Lhala - Gestão de Água</h1>
          </div>
          <div className="flex items-center gap-4">
            <div className="flex items-center gap-2">
              <UserCircle size={24} />
              <span>{user.nome}</span>
            </div>
            <button
              onClick={() => setUser(null)}
              className="bg-red-500 hover:bg-red-600 px-4 py-2 rounded flex items-center gap-2"
            >
              <LogOut size={18} />
              Sair
            </button>
          </div>
        </div>
      </header>

      <nav className="bg-white shadow-md">
        <div className="container mx-auto px-4">
          <div className="flex gap-1">
            <TabButton
              icon={Users}
              label="Utentes"
              active={activeTab === 'utentes'}
              onClick={() => setActiveTab('utentes')}
            />
            <TabButton
              icon={Droplets}
              label="Consumos"
              active={activeTab === 'consumos'}
              onClick={() => setActiveTab('consumos')}
            />
            <TabButton
              icon={FileText}
              label="Facturas"
              active={activeTab === 'facturas'}
              onClick={() => setActiveTab('facturas')}
            />
            <TabButton
              icon={DollarSign}
              label="Pagamentos"
              active={activeTab === 'pagamentos'}
              onClick={() => setActiveTab('pagamentos')}
            />
          </div>
        </div>
      </nav>

      <main className="container mx-auto px-4 py-6">
        {activeTab === 'utentes' && <UtentesTab />}
        {activeTab === 'consumos' && <ConsumosTab />}
        {activeTab === 'facturas' && <FacturasTab />}
        {activeTab === 'pagamentos' && <PagamentosTab />}
      </main>
    </div>
  );
}

// Componente de Login
function LoginScreen({ onLogin }) {
  const [login, setLogin] = useState('');
  const [senha, setSenha] = useState('');
  const [tipo, setTipo] = useState('admin');
  const [erro, setErro] = useState('');
  const [testandoConexao, setTestandoConexao] = useState(false);

  // Função para testar conexão com o backend
  const testarConexaoBackend = async () => {
    setTestandoConexao(true);
    try {
      const response = await fetch(`${API_BASE}/health`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });
      
      if (response.ok) {
        const message = await response.text();
        alert(`✅ Backend conectado: ${message}`);
      } else {
        alert(`❌ Backend respondeu com erro: ${response.status}`);
      }
    } catch (error) {
      alert(`❌ Não foi possível conectar ao backend: ${error.message}\n\nCertifique-se de que:\n1. O backend Spring Boot está rodando\n2. Na porta 8086\n3. O banco de dados está conectado`);
    }
    setTestandoConexao(false);
  };

  const handleLogin = async () => {
    setErro('');
    
    try {
      const endpoint = tipo === 'admin' ? 'admins' : 'utentes';
      console.log('Tentando autenticar em:', `${API_BASE}/${endpoint}/autenticar`);
      
      const authRes = await fetch(`${API_BASE}/${endpoint}/autenticar`, {
        method: 'POST',
        headers: { 
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        },
        body: JSON.stringify({ login, senha })
      });
      
      console.log('Status da autenticação:', authRes.status);
      
      if (!authRes.ok) {
        throw new Error(`Erro na autenticação: ${authRes.status}`);
      }
      
      const autenticado = await authRes.json();
      console.log('Resultado autenticação:', autenticado);
      
      if (autenticado) {
        // Buscar dados do usuário
        console.log('Buscando dados do usuário...');
        const userRes = await fetch(`${API_BASE}/${endpoint}/login/${encodeURIComponent(login)}`, {
          headers: {
            'Accept': 'application/json'
          }
        });
        
        console.log('Status da busca do usuário:', userRes.status);
        
        if (userRes.status === 404) {
          throw new Error('Usuário não encontrado após autenticação');
        }
        
        if (!userRes.ok) {
          throw new Error(`Erro ao buscar usuário: ${userRes.status}`);
        }
        
        const userData = await userRes.json();
        console.log('Dados do usuário encontrado:', userData);
        onLogin({ ...userData, tipo });
      } else {
        setErro('Login ou senha incorretos');
      }
    } catch (error) {
      console.error('Erro completo no login:', error);
      setErro(`Erro: ${error.message}. Clique em "Testar Conexão" para verificar o backend.`);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-500 to-blue-700 flex items-center justify-center p-4">
      <div className="bg-white rounded-lg shadow-2xl p-8 w-full max-w-md">
        <div className="text-center mb-6">
          <Droplets size={64} className="mx-auto text-blue-600 mb-4" />
          <h2 className="text-3xl font-bold text-gray-800">Mati Lhala</h2>
          <p className="text-gray-600">Sistema de Gestão de Água</p>
        </div>
        
        <div className="space-y-4">
          <div>
            <label className="block text-sm font-medium mb-2">Tipo de Acesso</label>
            <select
              value={tipo}
              onChange={(e) => setTipo(e.target.value)}
              className="w-full border rounded-lg px-4 py-2"
            >
              <option value="admin">Administrador</option>
              <option value="utente">Utente</option>
            </select>
          </div>
          
          <div>
            <label className="block text-sm font-medium mb-2">Login</label>
            <input
              type="text"
              value={login}
              onChange={(e) => setLogin(e.target.value)}
              className="w-full border rounded-lg px-4 py-2"
              placeholder="Digite seu login"
            />
          </div>
          
          <div>
            <label className="block text-sm font-medium mb-2">Senha</label>
            <input
              type="password"
              value={senha}
              onChange={(e) => setSenha(e.target.value)}
              className="w-full border rounded-lg px-4 py-2"
              placeholder="Digite sua senha"
            />
          </div>
          
          {erro && (
            <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
              {erro}
            </div>
          )}
          
          <button
            onClick={handleLogin}
            className="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 rounded-lg transition"
          >
            Entrar
          </button>
          
          <button
            type="button"
            onClick={testarConexaoBackend}
            disabled={testandoConexao}
            className="w-full bg-gray-500 hover:bg-gray-600 text-white font-bold py-3 rounded-lg transition disabled:opacity-50"
          >
            {testandoConexao ? 'Testando...' : 'Testar Conexão com Backend'}
          </button>
          
          <div className="text-center text-sm text-gray-600 mt-4">
            <p>Backend esperado: {API_BASE}</p>
            <p>Status: {testandoConexao ? 'Testando...' : 'Clique para testar'}</p>
          </div>
        </div>
      </div>
    </div>
  );
}

// Tab de Utentes
function UtentesTab() {
  const [utentes, setUtentes] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [editando, setEditando] = useState(null);
  const [form, setForm] = useState({
    nome: '', celular: '', endereco: '', login: '', senha: ''
  });

  useEffect(() => {
    carregarUtentes();
  }, []);

  const carregarUtentes = async () => {
    try {
      const res = await fetch(`${API_BASE}/utentes`);
      const data = await res.json();
      console.log('Utentes carregados:', data);
      setUtentes(data);
    } catch (error) {
      console.error('Erro ao carregar utentes:', error);
    }
  };

  const salvar = async () => {
    try {
      const url = editando ? `${API_BASE}/utentes/${editando}` : `${API_BASE}/utentes`;
      const method = editando ? 'PUT' : 'POST';
      
      await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(form)
      });
      
      setShowForm(false);
      setEditando(null);
      setForm({ nome: '', celular: '', endereco: '', login: '', senha: '' });
      carregarUtentes();
    } catch (error) {
      console.error('Erro ao salvar utente:', error);
      alert('Erro ao salvar utente');
    }
  };

  const editar = (utente) => {
    setForm(utente);
    setEditando(utente.id);
    setShowForm(true);
  };

  const deletar = async (id) => {
    if (confirm('Confirma exclusão?')) {
      try {
        await fetch(`${API_BASE}/utentes/${id}`, { method: 'DELETE' });
        carregarUtentes();
      } catch (error) {
        console.error('Erro ao excluir utente:', error);
        alert('Erro ao excluir utente');
      }
    }
  };

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-bold">Gestão de Utentes</h2>
        <button
          onClick={() => setShowForm(!showForm)}
          className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-lg"
        >
          {showForm ? 'Cancelar' : '+ Novo Utente'}
        </button>
      </div>

      {showForm && (
        <div className="bg-white p-6 rounded-lg shadow-md mb-6">
          <div className="grid grid-cols-2 gap-4">
            <input
              placeholder="Nome"
              value={form.nome}
              onChange={(e) => setForm({ ...form, nome: e.target.value })}
              className="border rounded px-4 py-2"
            />
            <input
              placeholder="Celular"
              value={form.celular}
              onChange={(e) => setForm({ ...form, celular: e.target.value })}
              className="border rounded px-4 py-2"
            />
            <input
              placeholder="Endereço"
              value={form.endereco}
              onChange={(e) => setForm({ ...form, endereco: e.target.value })}
              className="border rounded px-4 py-2"
            />
            <input
              placeholder="Login"
              value={form.login}
              onChange={(e) => setForm({ ...form, login: e.target.value })}
              className="border rounded px-4 py-2"
            />
            <input
              type="password"
              placeholder="Senha"
              value={form.senha}
              onChange={(e) => setForm({ ...form, senha: e.target.value })}
              className="border rounded px-4 py-2"
            />
          </div>
          <button onClick={salvar} className="mt-4 bg-green-600 hover:bg-green-700 text-white px-6 py-2 rounded">
            Salvar
          </button>
        </div>
      )}

      <div className="bg-white rounded-lg shadow-md overflow-hidden">
        <table className="w-full">
          <thead className="bg-gray-100">
            <tr>
              <th className="px-6 py-3 text-left">Nome</th>
              <th className="px-6 py-3 text-left">Celular</th>
              <th className="px-6 py-3 text-left">Endereço</th>
              <th className="px-6 py-3 text-left">Login</th>
              <th className="px-6 py-3 text-left">Ações</th>
            </tr>
          </thead>
          <tbody>
            {utentes.map((u) => (
              <tr key={u.id} className="border-t hover:bg-gray-50">
                <td className="px-6 py-4">{u.nome || 'N/A'}</td>
                <td className="px-6 py-4">{u.celular || 'N/A'}</td>
                <td className="px-6 py-4">{u.endereco || 'N/A'}</td>
                <td className="px-6 py-4">{u.login || 'N/A'}</td>
                <td className="px-6 py-4">
                  <button onClick={() => editar(u)} className="text-blue-600 hover:underline mr-4">
                    Editar
                  </button>
                  <button onClick={() => deletar(u.id)} className="text-red-600 hover:underline">
                    Excluir
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

// Tab de Consumos
function ConsumosTab() {
  const [consumos, setConsumos] = useState([]);
  const [utentes, setUtentes] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState({
    data: '', quantidade: '', tipoConsumo: 'Água', utente: { id: '' }
  });

  useEffect(() => {
    carregarConsumos();
    carregarUtentes();
  }, []);

  const carregarConsumos = async () => {
    try {
      const res = await fetch(`${API_BASE}/consumos`);
      const data = await res.json();
      console.log('Consumos carregados:', data);
      setConsumos(data);
    } catch (error) {
      console.error('Erro ao carregar consumos:', error);
    }
  };

  const carregarUtentes = async () => {
    try {
      const res = await fetch(`${API_BASE}/utentes`);
      const data = await res.json();
      setUtentes(data);
    } catch (error) {
      console.error('Erro ao carregar utentes:', error);
    }
  };

  const salvar = async () => {
    try {
      await fetch(`${API_BASE}/consumos`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(form)
      });
      
      setShowForm(false);
      setForm({ data: '', quantidade: '', tipoConsumo: 'Água', utente: { id: '' } });
      carregarConsumos();
    } catch (error) {
      console.error('Erro ao salvar consumo:', error);
      alert('Erro ao registrar consumo');
    }
  };

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-bold">Registro de Consumos</h2>
        <button
          onClick={() => setShowForm(!showForm)}
          className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-lg"
        >
          {showForm ? 'Cancelar' : '+ Novo Consumo'}
        </button>
      </div>

      {showForm && (
        <div className="bg-white p-6 rounded-lg shadow-md mb-6">
          <div className="grid grid-cols-2 gap-4">
            <select
              value={form.utente.id}
              onChange={(e) => setForm({ ...form, utente: { id: e.target.value } })}
              className="border rounded px-4 py-2"
            >
              <option value="">Selecione o Utente</option>
              {utentes.map((u) => (
                <option key={u.id} value={u.id}>{u.nome || 'N/A'}</option>
              ))}
            </select>
            <input
              type="date"
              value={form.data}
              onChange={(e) => setForm({ ...form, data: e.target.value })}
              className="border rounded px-4 py-2"
            />
            <input
              type="number"
              step="0.01"
              placeholder="Quantidade (m³)"
              value={form.quantidade}
              onChange={(e) => setForm({ ...form, quantidade: e.target.value })}
              className="border rounded px-4 py-2"
            />
            <input
              placeholder="Tipo de Consumo"
              value={form.tipoConsumo}
              onChange={(e) => setForm({ ...form, tipoConsumo: e.target.value })}
              className="border rounded px-4 py-2"
            />
          </div>
          <button onClick={salvar} className="mt-4 bg-green-600 hover:bg-green-700 text-white px-6 py-2 rounded">
            Registrar Consumo
          </button>
        </div>
      )}

      <div className="bg-white rounded-lg shadow-md overflow-hidden">
        <table className="w-full">
          <thead className="bg-gray-100">
            <tr>
              <th className="px-6 py-3 text-left">Data</th>
              <th className="px-6 py-3 text-left">Utente</th>
              <th className="px-6 py-3 text-left">Quantidade (m³)</th>
              <th className="px-6 py-3 text-left">Tipo</th>
            </tr>
          </thead>
          <tbody>
            {consumos.map((c) => (
              <tr key={c.id} className="border-t hover:bg-gray-50">
                <td className="px-6 py-4">{c.data || 'N/A'}</td>
                <td className="px-6 py-4">{c.utente?.nome || 'N/A'}</td>
                <td className="px-6 py-4">{c.quantidade || '0'}</td>
                <td className="px-6 py-4">{c.tipoConsumo || 'N/A'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

// Tab de Facturas
function FacturasTab() {
  const [facturas, setFacturas] = useState([]);
  const [utentes, setUtentes] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState({
    dataEmissao: '', consumo: '', valor: '', prazoPagamento: '', utente: { id: '' }
  });

  useEffect(() => {
    carregarFacturas();
    carregarUtentes();
  }, []);

  const carregarFacturas = async () => {
    try {
      const res = await fetch(`${API_BASE}/facturas`);
      const data = await res.json();
      console.log('Facturas carregadas:', data);
      setFacturas(data);
    } catch (error) {
      console.error('Erro ao carregar facturas:', error);
    }
  };

  const carregarUtentes = async () => {
    try {
      const res = await fetch(`${API_BASE}/utentes`);
      const data = await res.json();
      setUtentes(data);
    } catch (error) {
      console.error('Erro ao carregar utentes:', error);
    }
  };

  const gerarFactura = async () => {
    try {
      await fetch(`${API_BASE}/facturas/gerar`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(form)
      });
      
      setShowForm(false);
      setForm({ dataEmissao: '', consumo: '', valor: '', prazoPagamento: '', utente: { id: '' } });
      carregarFacturas();
    } catch (error) {
      console.error('Erro ao gerar factura:', error);
      alert('Erro ao gerar factura');
    }
  };

  // 🔥 CORREÇÃO: Função com verificação de null
  const calcularMulta = (factura) => {
    if (!factura || factura.paga) {
      return 0;
    }
    
    const hoje = new Date();
    const prazo = new Date(factura.prazoPagamento);
    
    // Verifica se as datas são válidas
    if (isNaN(hoje.getTime()) || isNaN(prazo.getTime())) {
      return 0;
    }
    
    if (hoje > prazo) {
      // Verifica se valor existe e é válido
      const valor = factura.valor || 0;
      return valor * 0.10;
    }
    return 0;
  };

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-bold">Gestão de Facturas</h2>
        <button
          onClick={() => setShowForm(!showForm)}
          className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-lg"
        >
          {showForm ? 'Cancelar' : '+ Gerar Factura'}
        </button>
      </div>

      {showForm && (
        <div className="bg-white p-6 rounded-lg shadow-md mb-6">
          <div className="grid grid-cols-2 gap-4">
            <select
              value={form.utente.id}
              onChange={(e) => setForm({ ...form, utente: { id: e.target.value } })}
              className="border rounded px-4 py-2"
            >
              <option value="">Selecione o Utente</option>
              {utentes.map((u) => (
                <option key={u.id} value={u.id}>{u.nome || 'N/A'}</option>
              ))}
            </select>
            <input
              type="date"
              placeholder="Data de Emissão"
              value={form.dataEmissao}
              onChange={(e) => setForm({ ...form, dataEmissao: e.target.value })}
              className="border rounded px-4 py-2"
            />
            <input
              type="number"
              step="0.01"
              placeholder="Consumo (m³)"
              value={form.consumo}
              onChange={(e) => setForm({ ...form, consumo: e.target.value })}
              className="border rounded px-4 py-2"
            />
            <input
              type="number"
              step="0.01"
              placeholder="Valor (MT)"
              value={form.valor}
              onChange={(e) => setForm({ ...form, valor: e.target.value })}
              className="border rounded px-4 py-2"
            />
            <input
              type="date"
              placeholder="Prazo de Pagamento"
              value={form.prazoPagamento}
              onChange={(e) => setForm({ ...form, prazoPagamento: e.target.value })}
              className="border rounded px-4 py-2"
            />
          </div>
          <button onClick={gerarFactura} className="mt-4 bg-green-600 hover:bg-green-700 text-white px-6 py-2 rounded">
            Gerar Factura
          </button>
        </div>
      )}

      <div className="bg-white rounded-lg shadow-md overflow-hidden">
        <table className="w-full">
          <thead className="bg-gray-100">
            <tr>
              <th className="px-6 py-3 text-left">Utente</th>
              <th className="px-6 py-3 text-left">Emissão</th>
              <th className="px-6 py-3 text-left">Consumo</th>
              <th className="px-6 py-3 text-left">Valor</th>
              <th className="px-6 py-3 text-left">Prazo</th>
              <th className="px-6 py-3 text-left">Status</th>
              <th className="px-6 py-3 text-left">Multa</th>
            </tr>
          </thead>
          <tbody>
            {facturas.map((f) => {
              // 🔥 CORREÇÃO: Verificações de segurança
              const multa = calcularMulta(f);
              const atrasada = multa > 0;
              const valor = f.valor || 0;
              const consumo = f.consumo || 0;
              const dataEmissao = f.dataEmissao || 'N/A';
              const prazoPagamento = f.prazoPagamento || 'N/A';
              const nomeUtente = f.utente?.nome || 'N/A';
              
              return (
                <tr key={f.id} className="border-t hover:bg-gray-50">
                  <td className="px-6 py-4">{nomeUtente}</td>
                  <td className="px-6 py-4">{dataEmissao}</td>
                  <td className="px-6 py-4">{consumo} m³</td>
                  <td className="px-6 py-4">{valor.toFixed(2)} MT</td>
                  <td className="px-6 py-4">{prazoPagamento}</td>
                  <td className="px-6 py-4">
                    <span className={`px-3 py-1 rounded-full text-sm ${
                      f.paga ? 'bg-green-100 text-green-800' : 
                      atrasada ? 'bg-red-100 text-red-800' : 
                      'bg-yellow-100 text-yellow-800'
                    }`}>
                      {f.paga ? 'Paga' : atrasada ? 'Atrasada' : 'Pendente'}
                    </span>
                  </td>
                  <td className="px-6 py-4">
                    {atrasada && (
                      <span className="text-red-600 font-semibold flex items-center gap-1">
                        <AlertTriangle size={16} />
                        {multa.toFixed(2)} MT
                      </span>
                    )}
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    </div>
  );
}

// Tab de Pagamentos
function PagamentosTab() {
  const [pagamentos, setPagamentos] = useState([]);
  const [facturas, setFacturas] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState({
    data: '', valor: '', metodo: 'Dinheiro', estado: 'Confirmado',
    utente: { id: '' }, factura: { id: '' }
  });

  useEffect(() => {
    carregarPagamentos();
    carregarFacturas();
  }, []);

  const carregarPagamentos = async () => {
    try {
      const res = await fetch(`${API_BASE}/pagamentos`);
      const data = await res.json();
      console.log('Pagamentos carregados:', data);
      setPagamentos(data);
    } catch (error) {
      console.error('Erro ao carregar pagamentos:', error);
    }
  };

  const carregarFacturas = async () => {
    try {
      const res = await fetch(`${API_BASE}/facturas`);
      const data = await res.json();
      // 🔥 CORREÇÃO: Verificação de segurança para valor
      const facturasNaoPagas = data.filter(f => !f.paga && f.valor != null);
      console.log('Facturas não pagas:', facturasNaoPagas);
      setFacturas(facturasNaoPagas);
    } catch (error) {
      console.error('Erro ao carregar facturas:', error);
    }
  };

  const efectuarPagamento = async () => {
    try {
      await fetch(`${API_BASE}/pagamentos/efectuar`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(form)
      });
      
      setShowForm(false);
      setForm({ data: '', valor: '', metodo: 'Dinheiro', estado: 'Confirmado', utente: { id: '' }, factura: { id: '' } });
      carregarPagamentos();
      carregarFacturas();
    } catch (error) {
      console.error('Erro ao efectuar pagamento:', error);
      alert('Erro ao registrar pagamento');
    }
  };

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-bold">Registro de Pagamentos</h2>
        <button
          onClick={() => setShowForm(!showForm)}
          className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-lg"
        >
          {showForm ? 'Cancelar' : '+ Novo Pagamento'}
        </button>
      </div>

      {showForm && (
        <div className="bg-white p-6 rounded-lg shadow-md mb-6">
          <div className="grid grid-cols-2 gap-4">
            <select
              value={form.factura.id}
              onChange={(e) => {
                const facturaId = e.target.value;
                const factura = facturas.find(f => f.id == facturaId);
                setForm({
                  ...form,
                  factura: { id: facturaId },
                  utente: { id: factura?.utente?.id || '' },
                  valor: factura?.valor || ''
                });
              }}
              className="border rounded px-4 py-2"
            >
              <option value="">Selecione a Factura</option>
              {facturas.map((f) => (
                <option key={f.id} value={f.id}>
                  {f.utente?.nome || 'N/A'} - {(f.valor || 0).toFixed(2)} MT - {f.dataEmissao || 'N/A'}
                </option>
              ))}
            </select>
            <input
              type="date"
              value={form.data}
              onChange={(e) => setForm({ ...form, data: e.target.value })}
              className="border rounded px-4 py-2"
            />
            <input
              type="number"
              step="0.01"
              placeholder="Valor (MT)"
              value={form.valor}
              onChange={(e) => setForm({ ...form, valor: e.target.value })}
              className="border rounded px-4 py-2"
            />
            <select
              value={form.metodo}
              onChange={(e) => setForm({ ...form, metodo: e.target.value })}
              className="border rounded px-4 py-2"
            >
              <option value="Dinheiro">Dinheiro</option>
              <option value="M-Pesa">M-Pesa</option>
              <option value="Transferência">Transferência</option>
            </select>
          </div>
          <button onClick={efectuarPagamento} className="mt-4 bg-green-600 hover:bg-green-700 text-white px-6 py-2 rounded">
            Registrar Pagamento
          </button>
        </div>
      )}

      <div className="bg-white rounded-lg shadow-md overflow-hidden">
        <table className="w-full">
          <thead className="bg-gray-100">
            <tr>
              <th className="px-6 py-3 text-left">Data</th>
              <th className="px-6 py-3 text-left">Utente</th>
              <th className="px-6 py-3 text-left">Valor</th>
              <th className="px-6 py-3 text-left">Método</th>
              <th className="px-6 py-3 text-left">Estado</th>
            </tr>
          </thead>
          <tbody>
            {pagamentos.map((p) => {
              // 🔥 CORREÇÃO: Verificação de segurança para valor
              const valor = p.valor || 0;
              return (
                <tr key={p.id} className="border-t hover:bg-gray-50">
                  <td className="px-6 py-4">{p.data || 'N/A'}</td>
                  <td className="px-6 py-4">{p.utente?.nome || 'N/A'}</td>
                  <td className="px-6 py-4">{valor.toFixed(2)} MT</td>
                  <td className="px-6 py-4">{p.metodo || 'N/A'}</td>
                  <td className="px-6 py-4">
                    <span className="px-3 py-1 rounded-full text-sm bg-green-100 text-green-800">
                      {p.estado || 'Confirmado'}
                    </span>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    </div>
  );
}

// Componente auxiliar para botões de navegação
function TabButton({ icon: Icon, label, active, onClick }) {
  return (
    <button
      onClick={onClick}
      className={`flex items-center gap-2 px-6 py-4 font-medium transition ${
        active
          ? 'bg-blue-600 text-white'
          : 'text-gray-600 hover:bg-gray-100'
      }`}
    >
      <Icon size={20} />
      {label}
    </button>
  );
}